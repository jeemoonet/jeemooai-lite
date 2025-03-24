package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jeemoo.api.service.HomeService;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.config.AiConfig;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.validate.EditGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.utils.PromptTransfer;
import com.jeemoo.common.utils.redis.RedisUtils;
import com.jeemoo.system.domain.*;
import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.domain.bo.AiPromptTipsBo;
import com.jeemoo.system.domain.vo.*;
import com.jeemoo.system.param.*;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.service.*;
import com.jeemoo.system.utils.ChatRequestBodyUtils;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;

import static com.jeemoo.system.domain.AiRecord.getChatMessage;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/prompt")
public class ApiPromptController {

    private final IAiModelsService modelsService;
    private final IAiPromptService promptService;
    private final IAiPromptTipsService promptTipsService;
    private final VectorService vectorService;
    private final IAiCompanyService companyService;
    private final AiConfig aiConfig;
    private final IAiWindowService windowService;
    private final IAiRecordService recordService;
    private final IAiDocumentCategoryService documentCategoryService;
    private final IAiRecordFileService recordFileService;
    private final HomeService homeService;

    @Value("${spring.profiles.active}")
    private String active;


    @Log(title = "助手左侧菜单", businessType = BusinessType.API)
    @GetMapping("/menu")
    public R<Object> menu() {
        List<MenuTree> tree = promptService.tree();
        return R.ok(tree);
    }

    @Log(title = "助手列表", businessType = BusinessType.API)
    @GetMapping("/page")
    public TableDataInfo<PromptListResponse> page(PromptSearchParam param, PageQuery page) {
        LoginUser user = LoginHelper.getLoginUser();
        param.setCompanyId(user.getCompanyId());
        param.setUserId(user.getUserId());
        param.setIsManager(companyService.isManager(user));
        return promptService.page(page, param);
    }


    @Log(title = "提示器详情", businessType = BusinessType.API)
    @GetMapping("")
    public R<Object> info(Long promptId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiPromptVo promptVo = promptService.info(promptId);
        if (!promptVo.getCompanyId().equals(loginUser.getCompanyId())) {
            return R.fail("提示器不在本企业");
        }
        return R.ok(promptVo);
    }

    @Log(title = "创建提示器", businessType = BusinessType.API)
    @PostMapping("/create")
    public R<Object> create(@RequestBody @Validated PromptCreateParam param) throws Exception {
        LoginUser user = LoginHelper.getLoginUser();
        if (StrUtil.isEmpty(param.getModel())) {
            List<AiModels> models = modelsService.list();
            for (AiModels model : models) {
                if (model.getIsDefault() == 1) {
                    param.setModel(String.valueOf(model.getId()));
                }
            }
            if (StrUtil.isEmpty(param.getModel())) {
                param.setModel(String.valueOf(models.get(0).getId()));
            }
        }
        param.setLoginUser(user);
        if (param.getCategoryId() == null || param.getCategoryId() == 0L) {
            List<AiPromptCategoryVo> list = homeService.categoryList();
            if (!list.isEmpty()) {
                param.setCategoryId(list.get(0).getCategoryId());
            }
        }
        if (promptService.create(param)) {
            return R.ok(param);
        } else {
            return R.fail();
        }
    }

    @Log(title = "创建提示器副本", businessType = BusinessType.API)
    @PostMapping("/copy")
    public R<Object> copy(@RequestBody @Validated PromptCreateParam param) throws Exception {
        LoginUser user = LoginHelper.getLoginUser();
        param.setLoginUser(user);
        if (promptService.copy(param)) {
            return R.ok(param);
        } else {
            return R.fail();
        }
    }

    @Log(title = "修改提示器", businessType = BusinessType.API)
    @PostMapping("/baseInfo/update")
    public R<Object> baseInfoUpdate(@RequestBody @Validated PromptCreateParam param) throws Exception {
        LoginUser user = LoginHelper.getLoginUser();
        AiCompanyVo companyVo = companyService.queryById(user.getCompanyId());
        param.setLoginUser(user);
        if (param.getPromptId() == null) {
            return R.fail("提示器id不能为空");
        }
        AiPromptVo prompt = promptService.queryById(param.getPromptId());
        if (prompt == null) {
            return R.fail("提示器不存在");
        }
        if (promptService.updateBaseInfo(param)) {
            return R.ok(param);
        } else {
            return R.fail();
        }
    }

    @Log(title = "修改提示器", businessType = BusinessType.API)
    @PostMapping("/update")
    public R<Object> update(@RequestBody @Validated(EditGroup.class) PromptCreateParam param) throws Exception {
        LoginUser user = LoginHelper.getLoginUser();
        AiCompanyVo companyVo = companyService.queryById(user.getCompanyId());
        param.setLoginUser(user);
        if (param.getPromptId() == null) {
            return R.fail("提示器id不能为空");
        }
        AiPromptVo prompt = promptService.queryById(param.getPromptId());
        if (prompt == null) {
            return R.fail("提示器不存在");
        }
        if (param.getPromptType() == 0 && StrUtil.isEmpty(param.getInitPrompt())) {
            return R.fail("角色设置不能为空");
        }
//        if (param.getIsShare()+param.getIsMinprogramShare()+param.getIsRobotShare()+param.getIsDigitPersonShare() > 0) {
//            companyService.checkShareCount(companyVo,param.getPromptId());
//        }
        param.setIsShare(null);
        param.setIsMinprogramShare(null);
        param.setIsRobotShare(null);
        param.setIsDigitPersonShare(null);
        param.setIsRtcShare(null);
        if (promptService.update(param)) {
            return R.ok(param);
        } else {
            return R.fail();
        }
    }

    @Log(title = "删除提示器", businessType = BusinessType.API)
    @DeleteMapping("")
    public R<Object> delete(Long promptId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiPromptVo prompt = promptService.queryById(promptId);
        if (prompt == null) {
            return R.fail("提示器不存在");
        }
        AiCompanyVo companyVo = companyService.queryById(loginUser.getCompanyId());
        if (!Objects.equals(companyVo.getUserId(), loginUser.getUserId())) {
            if (!Objects.equals(prompt.getUserId(), loginUser.getUserId())) {
                return R.fail("删除失败");
            }
        }

        if (promptService.deleteById(promptId)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "常用提示语", businessType = BusinessType.API)
    @GetMapping("/tips")
    public R<List<AiPromptTipsVo>> tips(Long promptId) {
        AiPromptTipsBo bo = new AiPromptTipsBo();
        bo.setPromptId(promptId);
        List<AiPromptTipsVo> list = promptTipsService.queryList(bo);
        return R.ok(list);
    }

    @Log(title = "上下架", businessType = BusinessType.API)
    @PostMapping("/status")
    public R<Object> status(@RequestBody JSONObject param) {
        Long promptId = param.getLong("promptId");
        Integer status = param.getInt("status");
        if (promptId == null) {
            return R.fail("提示器id不能为null");
        }
        AiPrompt prompt = promptService.fineById(promptId);
        if (prompt == null) {
            return R.fail("提示器不存在");
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (!Objects.equals(prompt.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail("提示器不属于当前企业");
        }
        if (promptService.setStatus(promptId, status)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "测试模型", businessType = BusinessType.API)
    @PostMapping("/test/sendMessage")
    public R<Object> modelsTest(@RequestBody @Validated SendTestMessageParam param) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<AiModels> models = modelsService.list();

        AiPrompt prompt = promptService.fineById(param.getPromptId());
        List<VectorData> vectorDataList = null;

        List<Long> documentIds = documentCategoryService.findDocumentIds(JSONUtil.toList(prompt.getDocCategoryIds(),String.class),loginUser.getCompanyId());
        if (documentIds != null && !documentIds.isEmpty()) {
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
        }

        String uuid = StrUtil.isNotEmpty(param.getUuid()) ? param.getUuid() : UUID.randomUUID().toString();
        List<AiWindow> windows = windowService.fetchTestWindow(param,loginUser, uuid);
        List<Long> windowIds = new ArrayList<>();
        for (AiWindow window : windows) {
            windowIds.add(window.getWindowId());
            prompt.setModel(window.getModel());
            List<ChatMessage> messages = recordService.fetchMessages(window,loginUser, prompt.getHistoryCount());

            ChatMessage question = new ChatMessage();
            if (param.getFilesUuid() != null && !param.getFilesUuid().isEmpty()) {
                String fileContent = recordFileService.getFileContent(param.getFilesUuid());
                question.setContent(param.getMessage() + "\n" + fileContent);
            } else {
                question.setContent(param.getMessage());
            }
            question.setRole(ChatMessageRole.USER.value());
            messages.add(question);

            Integer promptIsContext = prompt.getIsContext();
            if ((promptIsContext) == 0) {
                messages = new ArrayList<>();
                messages.add(question);
            }

            JSONObject functionResult = null;
//            List<AiFunctionVo> functionVos = functionPromptService.listByPromptId(prompt.getPromptId());
//            if (functionVos != null && !functionVos.isEmpty()) {
//                List<JSONObject> functions = new ArrayList<>();
//                JSONObject functionConfig = new JSONObject();
//                for (AiFunctionVo chatFunctionVo : functionVos) {
//                    JSONObject function = new JSONObject();
//                    function.set("name", chatFunctionVo.getFunctionName());
//                    function.set("description", chatFunctionVo.getDescription());
//                    function.set("parameters", JSONUtil.parseObj(chatFunctionVo.getParameters()));
//                    functions.add(function);
//
//                    JSONObject config = new JSONObject();
//                    config.set("actionUrl",chatFunctionVo.getActionUrl());
//                    config.set("email",chatFunctionVo.getEmail());
//                    config.set("className",chatFunctionVo.getClassName());
//                    functionConfig.set(chatFunctionVo.getFunctionName(),config);
//                }
//                functionResult = openaiService.functionCall(messages, functions);
//            }

            ChatMessage message = getChatMessage(prompt, vectorDataList, null);
//            if (functionResult != null) {
//                message.setContent(message.getContent() + "\n" + functionResult.getStr("functionResult"));
//            }
            message.setContent(PromptTransfer.transfer(message.getContent(), "${history}", messages, false));
            message.setContent(PromptTransfer.transfer(message.getContent(), "${query}", param.getMessage(), false));
            message.setContent(PromptTransfer.transfer(message.getContent(), "${nickName}", loginUser.getNickName(), false));
            messages.add(0,message);


            JSONObject requestBody = new JSONObject();
            requestBody.set("messages",messages);
            requestBody.set("model",window.getModel());
            ChatRequestBodyUtils.parse(requestBody,prompt,messages,models);

            AiRecord record = recordService.insertRecord(param.getMessage(),window,vectorDataList,loginUser.getUserId());
            if (param.getFilesUuid() != null && !param.getFilesUuid().isEmpty()) {
                recordFileService.updateRecordId(record, param.getFilesUuid());
            }
            saveToRedis(window,loginUser,record,requestBody, prompt==null?null:prompt.getVoice());
        }

        JSONObject response = new JSONObject();
        response.set("uuid", uuid);
        response.set("windowIds",windowIds);
        response.set("windows",windows);
        return R.ok(response);
    }

    private void saveToRedis(AiWindow window, LoginUser loginUser, AiRecord record, JSONObject requestBody, String voice) {
        JSONObject redisData = new JSONObject();
        if (StrUtil.isEmpty(voice)) {
            voice = "zhimiao_emo";
        }
        redisData.set("voice",voice);
        redisData.set("record",record);
        redisData.set("requestBody",requestBody);
        RedisUtils.setCacheObject(loginUser.getLoginId()+'_'+window.getWindowId(),JSONUtil.toJsonStr(redisData), Duration.ofHours(1));
    }
}
