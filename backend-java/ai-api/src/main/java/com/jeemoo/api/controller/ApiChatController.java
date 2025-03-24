package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jeemoo.common.config.AiConfig;
import com.jeemoo.common.core.service.ConfigService;
import com.jeemoo.common.utils.PromptTransfer;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.utils.file.FileUploadUtils;
import com.jeemoo.system.domain.*;
import com.jeemoo.system.param.ChatPromptSelectResponse;
import com.jeemoo.system.utils.ChatRequestBodyUtils;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.common.utils.redis.RedisUtils;
import com.jeemoo.system.domain.bo.AiRecordBo;
import com.jeemoo.system.domain.vo.AiRecordVo;
import com.jeemoo.system.param.SendMessageParam;
import com.jeemoo.system.param.SendMessageResponse;
import com.jeemoo.system.param.VectorData;
import com.jeemoo.system.service.*;
import com.theokanning.openai.completion.chat.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.jeemoo.system.domain.AiRecord.getChatMessage;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ApiChatController {

    private final IAiWindowService windowService;
    private final IAiRecordService recordService;
    private final IAiPromptService promptService;
    private final IAiDocumentCategoryService documentCategoryService;
    private final VectorService vectorService;
    private final AiConfig aiConfig;
    private final IAiModelsService modelsService;
    private final IAiRecordFileService recordFileService;
    private final HttpServletRequest request;
    private final ConfigService configService;
    private Logger logger = LoggerFactory.getLogger(ApiChatController.class);

    private String[] imageExt = new String[]{"png","jpg","jpeg"};
    private String[] pdfExt = new String[]{"pdf"};
    private String[] docExt = new String[]{"docx","doc"};
    private String[] voiceExt = new String[]{"mp3","mp4","wav"};

    @Log(title = "历史记录", businessType = BusinessType.API)
    @GetMapping("/history")
    public TableDataInfo<AiRecordVo> history(Long windowId, Long timestamp, PageQuery pageQuery) {
        if (timestamp == null) {
            timestamp = 0L;
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiRecordBo bo = new AiRecordBo();
        bo.setUserId(loginUser.getUserId());
        bo.setWindowId(windowId);
        bo.setTimestamp(timestamp);

        TableDataInfo<AiRecordVo> result = recordService.queryPageList(bo, pageQuery);
        List<AiRecordVo> rows = result.getRows();
        if (rows.isEmpty()) {
            return result;
        }
        List<Long> recordIds = new ArrayList<>();
        for (AiRecordVo row : rows) {
            recordIds.add(row.getMessageId());
        }
        List<AiRecordFile> recordFiles = recordFileService.getFileByRecordIds(recordIds);
        for (AiRecordVo row : rows) {
            for (AiRecordFile recordFile : recordFiles) {
                if (row.getMessageId().equals(recordFile.getRecordId())) {
                    row.addRecordFile(recordFile);
                }
            }
            if (StrUtil.isNotEmpty(row.getDocInfo())) {
                row.setDocInfoList(JSONUtil.toList(row.getDocInfo(),JSONObject.class));
            }
            if (StrUtil.isNotEmpty(row.getSearchInfo())) {
                row.setSearchInfoList(JSONUtil.toList(row.getSearchInfo(), JSONObject.class));
            }
        }
        result.setRows(rows);
        return result;
    }

    @Log(title = "发送消息", businessType = BusinessType.API)
    @PostMapping("/sendMessage")
    public R<SendMessageResponse> sendMessage(@RequestBody @Validated SendMessageParam param) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        String platform = request.getHeader("Platform");
        if (platform == null) {
            platform = "pc";
        }
        param.setPlatform(platform);
        List<AiModels> models = modelsService.list();
        AiModels defaultModel = new AiModels();
        for (AiModels model : models) {
            if (param.getIsReasoning() == 1 && model.getIsReasoningDefault() == 1) {
                defaultModel = model;
                break;
            }
            if (param.getIsReasoning() == 0 && model.getIsDefault() == 1) {
                defaultModel = model;
                break;
            }
        }
        SendMessageResponse response = new SendMessageResponse();
        AiWindow window = windowService.fetchWindow(param, loginUser, platform);
        AiPrompt prompt = promptService.fetchPrompt(window,loginUser);
        int historyCount = 6;
        if (prompt != null) {
            historyCount = prompt.getHistoryCount();
        }
        List<ChatMessage> messages = recordService.fetchMessages(window, loginUser, historyCount);

        ChatMessage question = new ChatMessage();
        String fileContent = "";
        if (param.getFilesUuid() != null && !param.getFilesUuid().isEmpty()) {
            fileContent = recordFileService.getFileContent(param.getFilesUuid());
            fileContent += param.getMessage();
        }
        question.setContent(param.getMessage());

        question.setRole(ChatMessageRole.USER.value());
        messages.add(question);

        Integer promptIsContext = 1;
        if (prompt != null) {
            promptIsContext = prompt.getIsContext();
        }
        if ((promptIsContext * param.getIsContext()) == 0) {
            messages = new ArrayList<>();
            messages.add(question);
        }

        List<VectorData> vectorDataList = new ArrayList<>();
        if (prompt != null) {
            promptService.recent(prompt, loginUser);
            response.setPromptId(prompt.getPromptId());
            response.setPromptIcon(prompt.getPromptIcon());
            response.setPromptName(prompt.getPromptName());
            response.setPromptDesc(prompt.getPromptDesc());
            List<Long> documentIds = documentCategoryService.findDocumentIds(JSONUtil.toList(prompt.getDocCategoryIds(),String.class),loginUser.getCompanyId());
            HashMap<String, VectorData> hashMap = new HashMap<>();
            if (documentIds != null && !documentIds.isEmpty()) {
                if (prompt.getSearchType().equals("vector_search") || prompt.getSearchType().equals("hybrid_search")) {
                    vectorDataList = vectorService.embeddingSearch(param.getMessage(), documentIds, "company_" + loginUser.getCompanyId(), prompt.getTopK(), prompt.getScore());
                    for (VectorData vectorData : vectorDataList) {
                        hashMap.put(vectorData.getId(),vectorData);
                    }
                }
                if (prompt.getSearchType().equals("full_text_search") || prompt.getSearchType().equals("hybrid_search")) {
                    vectorDataList = vectorService.fullTextSearch(param.getMessage(), documentIds, "company_" + loginUser.getCompanyId(), prompt.getTopK());
                    for (VectorData vectorData : vectorDataList) {
                        hashMap.put(vectorData.getId(),vectorData);
                    }
                }
                vectorDataList = new ArrayList<>(hashMap.values());
            }
            List<VectorData> longHistoryList = null;
            if (prompt.getLongHistory() == 1) {
                longHistoryList = vectorService.longHistorySearch(param.getMessage(), window.getWindowId(), loginUser.getUserId(), 4, null);
            }
            ChatMessage message = getChatMessage(prompt, vectorDataList, longHistoryList);
            if (StringUtils.isNotEmpty(fileContent)) {
                message.setContent(PromptTransfer.transfer(message.getContent(), "${file}",  fileContent, true));
            }
            message.setContent(PromptTransfer.transfer(message.getContent(), "${history}", messages, false));
            message.setContent(PromptTransfer.transfer(message.getContent(), "${query}", param.getMessage(), false));
            message.setContent(PromptTransfer.transfer(message.getContent(), "${nickName}", loginUser.getNickName(), false));
            messages.add(0,message);
        } else {
            if (StringUtils.isNotEmpty(fileContent)) {
                ChatMessage message = new ChatMessage();
                message.setRole(ChatMessageRole.SYSTEM.value());
                message.setContent(fileContent);
                messages.add(0, message);
            }
        }

        JSONObject requestBody = new JSONObject();
        requestBody.set("messages",messages);
        requestBody.set("model",defaultModel.getModelLabel());
        if (prompt != null) {
            ChatRequestBodyUtils.parse(requestBody,prompt,messages,models);
        }

        Integer isNeedSearch = param.getIsNeedSearch();
        String isNeedSearchConfig = configService.getConfigValue("is_need_search");
        if (StringUtils.isNotEmpty(isNeedSearchConfig)) {
            isNeedSearch = isNeedSearch & Integer.parseInt(isNeedSearchConfig);
        }
        requestBody.set("isNeedSearch", isNeedSearch);
        AiRecord record = recordService.insertRecord(param,window,vectorDataList,loginUser.getUserId(), null, null);
        saveToRedis(loginUser,record,requestBody, prompt==null?null:prompt.getVoice(), window);

        response.setMessageId(record.getMessageId());
        response.setMasterUuid(record.getMasterUuid());
        response.setDocInfo(record.getDocInfoList());
        response.setWindowName(window.getWindowName());
        response.setWindowId(window.getWindowId());
        if (prompt != null && prompt.getPromptType() == 1) {
            response.setNeedSearch(true);
        } else {
            response.setNeedSearch(isNeedSearch == 1);
        }

        return R.ok(response);
    }

    private void saveToRedis(LoginUser loginUser, AiRecord record, JSONObject requestBody, String voice, AiWindow window) {
        JSONObject redisData = new JSONObject();
        if (StrUtil.isEmpty(voice)) {
            voice = "zhimiao_emo";
        }
        redisData.set("voice",voice);
        redisData.set("record",record);
        redisData.set("requestBody",requestBody);
        redisData.set("window", window);
        RedisUtils.setCacheObject(loginUser.getLoginId(),JSONUtil.toJsonStr(redisData), Duration.ofHours(1));
    }

    @GetMapping("/prompt")
    public R<Object> prompt() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<ChatPromptSelectResponse> list = promptService.chatSelect(loginUser);
        return R.ok(list);
    }


    @Log(title = "上传聊天文件", businessType = BusinessType.API)
    @PostMapping("/upload/file")
    public R<Object> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        MultipartFile[] files = new MultipartFile[]{file};
        return this.upload(files);
    }

    @Log(title = "上传聊天文件", businessType = BusinessType.API)
    @PostMapping("/upload")
    public R<Object> upload(@RequestPart("files") MultipartFile[] files) throws IOException {
        List<AiRecordFile> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            AiRecordFile recordFile = new AiRecordFile();
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileType = this.getFileType(fileExtension.replace(".",""));
            if (StringUtils.isEmpty(fileType)) {
                return R.fail("不支持文件格式:"+originalFilename);
            }
            String path = FileUploadUtils.upload(file);
            File convFile = new File(AiConfig.getProfile() + path);
            recordFile.setFileUrl(path);

            if (fileType.equals("pdf")) {
                PDDocument document = PDDocument.load(convFile);
                if (!document.isEncrypted()) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    String pdfText = stripper.getText(document);
                    recordFile.setContent(pdfText);
                } else {
                    throw new IOException("pdf解析失败");
                }
            } else if (fileType.equals("doc")) {
                try {
                    FileInputStream fis = new FileInputStream(convFile.getAbsoluteFile());
                    if (fileExtension.equals(".docx")) {
                        XWPFDocument document = new XWPFDocument(fis);
                        List<XWPFParagraph> paragraphs = document.getParagraphs();
                        String docText = paragraphs.stream()
                                .map(XWPFParagraph::getText)
                                .collect(Collectors.joining("\n"));
                        recordFile.setContent(docText);
                    } else {
                        HWPFDocument document = new HWPFDocument(fis);
                        WordExtractor extractor = new WordExtractor(document);
                        String[] paragraphs = extractor.getParagraphText();
                        StringBuilder content = new StringBuilder();
                        for (String para : paragraphs) {
                            content.append(para);
                        }
                        extractor.close();
                        recordFile.setContent(content.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IOException("文档解析失败："+ originalFilename);
                }
            }
            recordFile.setFileName(originalFilename);
            recordFile.setFileSize(file.getSize());
            recordFile.setFileType(fileType);
            fileList.add(recordFile);
        }

        if (recordFileService.insertBatch(fileList)){
            for (AiRecordFile recordFile : fileList) {
                recordFile.setContent(null);
            }
            return R.ok(fileList);
        } else {
            return R.fail("解析失败");
        }
    }

    @Log(title = "清空上下文", businessType = BusinessType.API)
    @PostMapping("/clearContext")
    public R<Object> clearContext(@RequestBody JSONObject param) {
        Long windowId = param.getLong("windowId");
        if (windowId == null || windowId == 0L) {
            return R.fail("windowId错误");
        }
        AiWindow window = windowService.findOne(windowId);
        if (window == null) {
            return R.fail("window错误");
        }

        if (recordService.clearContext(window)) {
            return R.ok();
        } else {
            return R.fail("清除失败");
        }
    }


    private String getFileType(String fileExtension) {
        fileExtension = fileExtension.toLowerCase();

        if (isExtensionInArray(fileExtension, imageExt)) {
            return "image";
        } else if (isExtensionInArray(fileExtension, pdfExt)) {
            return "pdf";
        } else if (isExtensionInArray(fileExtension, docExt)) {
            return "doc";
        } else if (isExtensionInArray(fileExtension, voiceExt)) {
            return "voice";
        } else {
            return null;
        }
    }

    private boolean isExtensionInArray(String fileExtension, String[] extensionsArray) {
        for (String extension : extensionsArray) {
            if (fileExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
