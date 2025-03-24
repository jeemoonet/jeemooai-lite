package com.jeemoo.api.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.config.AiConfig;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.common.utils.DateUtils;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.utils.file.FileUploadUtils;
import com.jeemoo.common.utils.uuid.Seq;
import com.jeemoo.system.domain.AiDocument;
import com.jeemoo.system.domain.AiDocumentEntity;
import com.jeemoo.system.domain.bo.AiDocumentBo;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.system.domain.bo.AiSplitRuleBo;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.system.domain.vo.AiDocumentVo;
import com.jeemoo.system.domain.vo.AiSplitRuleVo;
import com.jeemoo.system.param.*;
import com.jeemoo.system.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/document")
public class ApiDocumentController {

    private final IAiDocumentCategoryService documentCategoryService;
    private final IAiDocumentService documentService;
    private final IAiCompanyService companyService;
    private final VectorService vectorService;
    private final IAiDocumentEntityService documentEntityService;
    private final IAiSplitRuleService splitRuleService;
    private final AiConfig aiConfig;

    @Log(title = "企业大脑左侧菜单", businessType = BusinessType.API)
    @GetMapping("/menu")
    public R<Object> menu() {
        AiDocumentCategoryBo bo = new AiDocumentCategoryBo();
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiCompanyVo companyVo = companyService.queryById(loginUser.getCompanyId());
        bo.setCompanyId(loginUser.getCompanyId());
        List<MenuTree> list = documentCategoryService.menuTree(bo, loginUser, companyVo);
        List<MenuTree> tree = MenuTree.buildCategoryTree(list);
        return R.ok(tree);
    }

    @Log(title = "文档列表",businessType = BusinessType.API)
    @GetMapping("/list")
    public TableDataInfo<AiDocumentVo> list(AiDocumentBo param, PageQuery pageQuery) {
        return documentService.apiPageList(param,pageQuery,LoginHelper.getLoginUser());
    }

    @Log(title = "文档详情",businessType = BusinessType.API)
    @GetMapping("/info")
    public R<AiDocumentVo> info(Long documentId) {
        AiDocumentVo documentVo = documentService.queryById(documentId);
        return R.ok(documentVo);
    }

    @Log(title = "文档列表",businessType = BusinessType.API)
    @GetMapping("/folder")
    public R<Object> folder(DocumentFolderSearchParam param) {
        List<AiDocumentVo> list = documentService.folder(param, LoginHelper.getLoginUser());
        return R.ok(list);
    }

    @Log(title = "删除文档", businessType = BusinessType.API)
    @DeleteMapping("")
    @Transactional
    public R<Object> delete(Long documentId) throws IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentVo documentVo = documentService.queryById(documentId);
        if (documentVo == null || !Objects.equals(documentVo.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail();
        }
        TableDataInfo<VectorData> vectorList = vectorService.listPage("company_"+loginUser.getCompanyId(), documentId, 1, 10000, null);
        List<String> ids = new ArrayList<>();
        for (VectorData row : vectorList.getRows()) {
            ids.add(row.getId());
        }
        if (!ids.isEmpty()) {
            vectorService.deleteVector("company_" + loginUser.getCompanyId(), ids);
        }
        if (documentService.deleteById(documentId, loginUser)) {
            documentEntityService.delete(new QueryWrapper<AiDocumentEntity>().eq("document_id",documentId));
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "上传url文档", businessType = BusinessType.API)
    @PostMapping("/url")
    public R<Object> url(@RequestBody @Validated DocumentUrlParam param) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentCategoryVo category = documentCategoryService.queryById(param.getCategoryId());
        if (param.getCategoryId() == 1L || param.getCategoryId() == 2L) {
            return R.fail("当前分类禁止上传文档");
        }
        if (!Objects.equals(category.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail("知识库分类错误，不属于当前企业。");
        }
        for (String url : param.getUrls()) {
            AiDocumentBo documentBo = new AiDocumentBo();
            documentBo.setCompanyId(loginUser.getCompanyId());
            documentBo.setCategoryId(param.getCategoryId());
            documentBo.setPageNumber(1);
            documentBo.setDocumentName("");
            documentBo.setDocumentType("url");
            documentBo.setUrl(url);
            documentBo.setFileSize(0L);
            documentBo.setUserId(loginUser.getUserId());
            documentBo.setCompanyName(loginUser.getCompanyName());
            documentBo.setCreateBy(loginUser.getNickName());
            documentService.insertByBo(documentBo);
        }

        return R.ok("上传成功");
    }

    @Log(title = "上传文字文档", businessType = BusinessType.API)
    @PostMapping("/text")
    public R<Object> text(@RequestBody @Validated DocumentTextParam param) throws IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentCategoryVo category = documentCategoryService.queryById(param.getCategoryId());

        String filename = UUID.randomUUID().toString();

        String path = StringUtils.format("{}/{}_{}.{}", DateUtils.datePath(),
                FilenameUtils.getBaseName(filename), Seq.getId(Seq.uploadSeqType), "txt");
        File tempFile = new File(AiConfig.getProfile() + "/" + path);

        // 使用FileWriter将字符串写入临时文件
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(param.getText());
            // 刷新输出流，确保全部内容都被写入文件
            writer.flush();
        }

        AiDocumentBo documentBo = new AiDocumentBo();
        documentBo.setCompanyId(loginUser.getCompanyId());
        documentBo.setCategoryId(category.getCategoryId());
        documentBo.setPageNumber(1);
        documentBo.setDocumentName(param.getText().substring(0,10));
        documentBo.setDocumentType("txt");
        documentBo.setUrl(aiConfig.getPreviewUrl() + "/" + path);
        documentBo.setFileSize(0L);
        documentBo.setUserId(loginUser.getUserId());
        documentBo.setCompanyName(loginUser.getCompanyName());
        documentBo.setCreateBy(loginUser.getNickName());
        documentService.insertByBo(documentBo);

        return R.ok("上传成功");
    }

    @Log(title = "上传wechat文档", businessType = BusinessType.API)
    @PostMapping("/wechat")
    public R<Object> wechat(@RequestBody @Validated DocumentUrlParam param) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentCategoryVo category = documentCategoryService.queryById(param.getCategoryId());
        if (param.getCategoryId() == 1L || param.getCategoryId() == 2L) {
            return R.fail("当前分类禁止上传文档");
        }
        if (!Objects.equals(category.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail("知识库分类错误，不属于当前企业。");
        }
        for (String url : param.getUrls()) {
            AiDocumentBo documentBo = new AiDocumentBo();
            documentBo.setCompanyId(loginUser.getCompanyId());
            documentBo.setCategoryId(param.getCategoryId());
            documentBo.setPageNumber(1);
            documentBo.setDocumentName("");
            documentBo.setDocumentType("wechat");
            documentBo.setUrl(url);
            documentBo.setFileSize(0L);
            documentBo.setUserId(loginUser.getUserId());
            documentBo.setCompanyName(loginUser.getCompanyName());
            documentBo.setCreateBy(loginUser.getNickName());
            documentService.insertByBo(documentBo);
//            jmsMessagingTemplate.convertAndSend("document_queue", JSONUtil.toJsonStr(documentBo));
        }

        return R.ok("上传成功");
    }

    @Log(title = "获取拆分策略列表", businessType = BusinessType.API)
    @GetMapping("/splitList")
    public R<Object> splitList() {
        List<AiSplitRuleVo> list = splitRuleService.queryList(new AiSplitRuleBo());
        List<Integer> select = new ArrayList<>();
        select.add(128);
        select.add(256);
        select.add(512);
        select.add(1024);
        select.add(2048);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("split",list);
        jsonObject.set("lengthSelect",select);
        return R.ok(jsonObject);
    }

    @Log(title = "获取拆分策略", businessType = BusinessType.API)
    @GetMapping("/splitInfo")
    public R<Object> splitInfo(Long documentId) {
        AiDocumentVo documentVo = documentService.queryById(documentId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("id",documentVo.getSplitId());
        jsonObject.set("splitChar",documentVo.getSplitChar());
        jsonObject.set("length",documentVo.getLength());
        jsonObject.set("name",documentVo.getSplitName());
        return R.ok(jsonObject);
    }

    @Log(title = "拆分文档", businessType = BusinessType.API)
    @PostMapping("/split")
    @Transactional
    public R<Object> split(@RequestBody @Validated SplitDocumentParam param) throws IOException {
        if (param.getLength() < 100 || param.getLength() > 5000) {
            return R.fail("拆分长度：100-5000");
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentVo documentVo = documentService.queryById(param.getDocumentId());
        if (documentVo == null || !Objects.equals(documentVo.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail();
        }
        TableDataInfo<VectorData> vectorList = vectorService.listPage("company_"+loginUser.getCompanyId(), param.getDocumentId(), 1, 10000, null);
        List<String> ids = new ArrayList<>();
        for (VectorData row : vectorList.getRows()) {
            ids.add(row.getId());
        }
        if (!ids.isEmpty()) {
            vectorService.deleteVector("company_" + loginUser.getCompanyId(), ids);
        }
        documentEntityService.delete(new QueryWrapper<AiDocumentEntity>().eq("document_id",param.getDocumentId()));
        if (documentService.split(param.getDocumentId(), param)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title= "向量搜索", businessType = BusinessType.API)
    @PostMapping("/search")
    public R<Object> search(@RequestBody @Validated VectorSearchParam param) throws IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (param.getTopK() == null) {
            param.setTopK(10);
        }
        List<VectorSearchResponse> list = new ArrayList<>();
        List<Long> documentIds = documentCategoryService.findDocumentIds(param.getCategoryIds(),loginUser.getCompanyId());
        if (documentIds.isEmpty()) {
            return R.ok(list);
        }
        HashMap<String, VectorData> hashMap = new HashMap<>();
        List<VectorData> vectorDataList = new ArrayList<>();
        if (documentIds != null && !documentIds.isEmpty()) {
            if (param.getSearchType().equals("vector_search") || param.getSearchType().equals("hybrid_search")) {
                vectorDataList = vectorService.embeddingSearch(param.getSearch(), documentIds, "company_" + loginUser.getCompanyId(), param.getTopK(), param.getScore());
                for (VectorData vectorData : vectorDataList) {
                    hashMap.put(vectorData.getId(),vectorData);
                }
            }
            if (param.getSearchType().equals("full_text_search") || param.getSearchType().equals("hybrid_search")) {
                vectorDataList = vectorService.fullTextSearch(param.getSearch(), documentIds, "company_" + loginUser.getCompanyId(), param.getTopK());
                for (VectorData vectorData : vectorDataList) {
                    hashMap.put(vectorData.getId(),vectorData);
                }
            }
            vectorDataList = new ArrayList<>(hashMap.values());
        }
        if (vectorDataList.isEmpty()) {
            return R.ok(list);
        }

        documentIds = new ArrayList<>();
        for (VectorData vectorData : vectorDataList) {
            documentIds.add(vectorData.getDocumentId());
        }

        List<AiDocument> documents = documentService.select(new QueryWrapper<AiDocument>().in("document_id",documentIds));
        HashMap<Long, String> documentNameMap = new HashMap<>();
        for (AiDocument document : documents) {
            documentNameMap.put(document.getDocumentId(),document.getDocumentName());
        }

        for (VectorData vectorData : vectorDataList) {
            VectorSearchResponse response = new VectorSearchResponse();
            response.setId(vectorData.getId());
            response.setContent(vectorData.getContent());
            response.setScore(vectorData.getScore());
            response.setDocumentName(documentNameMap.get(vectorData.getDocumentId()));
            list.add(response);
        }

        return R.ok(list);
    }

    @Log(title = "上传文件文档", businessType = BusinessType.API)
    @PostMapping("/upload")
    public R<Object> upload(@RequestParam("file") MultipartFile file, Long categoryId) throws IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentCategoryVo category = documentCategoryService.queryById(categoryId);
        if (categoryId == 1L || categoryId == 2L) {
            return R.fail("当前分类禁止上传文档");
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        List<String> allowExt = new ArrayList<>();
        allowExt.add(".pdf");
        allowExt.add(".docx");
        allowExt.add(".txt");
        if (!allowExt.contains(fileExtension.toLowerCase())) {
            return R.fail("不支持"+fileExtension+"类型,请上传:"+ StrUtil.join("|",allowExt)+"类型");
        }
        String path = FileUploadUtils.upload(file);

        AiDocumentBo documentBo = new AiDocumentBo();
        documentBo.setCompanyId(loginUser.getCompanyId());
        documentBo.setCategoryId(categoryId);
        documentBo.setPageNumber(1);
        documentBo.setDocumentName(originalFilename.replace(fileExtension,""));
        documentBo.setDocumentType(fileExtension.replace(".",""));
        documentBo.setUrl(aiConfig.getPreviewUrl() + path);
        documentBo.setFileSize(0L);
        documentBo.setUserId(loginUser.getUserId());
        documentBo.setCompanyName(loginUser.getCompanyName());
        documentBo.setCreateBy(loginUser.getNickName());
        documentService.insertByBo(documentBo);

//        jmsMessagingTemplate.convertAndSend("document_queue", JSONUtil.toJsonStr(documentBo));

        return R.ok("上传成功");
    }
}
