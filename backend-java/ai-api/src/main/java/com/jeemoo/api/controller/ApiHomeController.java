package com.jeemoo.api.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jeemoo.api.service.HomeService;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.config.AiConfig;
import com.jeemoo.common.core.controller.BaseController;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.common.utils.file.FileUploadUtils;
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.param.AiModels;
import com.jeemoo.system.param.PromptListResponse;
import com.jeemoo.system.param.PromptSearchParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/home")
public class ApiHomeController extends BaseController {

    private final HomeService homeService;
    private final AiConfig aiConfig;

    @Log(title = "首页推荐提示器", businessType = BusinessType.API)
    @GetMapping("/recommendList")
    public R<Object> recommendList() {
        List<PromptListResponse> list = homeService.recommendList(LoginHelper.getLoginUser());
        return R.ok(list);
    }

    @Log(title="最近使用", businessType = BusinessType.API)
    @GetMapping("/recent")
    public R<Object> recent() {
        String platform = getPlatform();
        List<PromptListResponse> list = homeService.recent(LoginHelper.getLoginUser(), platform);
        return R.ok(list);
    }

    @Log(title = "首页分类", businessType = BusinessType.API)
    @GetMapping("/categoryList")
    public R<Object> categoryList() {
        String platform = getPlatform();
        if("miniapp".equals(platform)){
            List<AiPromptCategoryVo> list = homeService.miniappCategoryList();
            return R.ok(list);
        }
        List<AiPromptCategoryVo> list = homeService.categoryList();
        return R.ok(list);
    }

    @Log(title = "首页提示器列表", businessType = BusinessType.API)
    @GetMapping("/promptList")
    public TableDataInfo<PromptListResponse> promptList(PromptSearchParam param, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        param.setCompanyId(loginUser.getCompanyId());
        return homeService.promptPage(param,pageQuery);
    }

    @Log(title = "文件上传", businessType = BusinessType.API,isSaveRequestData = false)
    @PostMapping("/upload/base64")
    public R<Object> uploadBase64(@RequestBody JSONObject param) throws IOException {
        String base64Str = param.getStr("base64");
        byte[] decodedBytes = Base64.getDecoder().decode(base64Str);
        File convFile = File.createTempFile("temp",".png");
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(decodedBytes);
        }

        String path = AiConfig.getProfile() + "/" + UUID.randomUUID() + ".png";
        convFile.renameTo(new File(path));


        JSONObject jsonObject = new JSONObject();
        jsonObject.set("url",path);
        jsonObject.set("path",path);
        return R.ok(jsonObject);
    }

    @Log(title = "文件上传", businessType = BusinessType.API, isSaveRequestData = false)
    @PostMapping("/multipart-upload/base64")
    public R<Object> uploadMultipartBase64(@RequestBody JSONObject param) throws IOException {
        JSONArray base64Files = param.getJSONArray("base64Files");  // 前端应当发送一个包含多个Base64字符串的JSON数组
        JSONArray uploadResults = new JSONArray();

        for (int i = 0; i < base64Files.size(); i++) {
            String base64Str = base64Files.getStr(i);
            byte[] decodedBytes = Base64.getDecoder().decode(base64Str);

            File convFile = File.createTempFile("temp",".png");
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(decodedBytes);
            }


            String path = AiConfig.getProfile() + "/" + UUID.randomUUID() + ".png";
            convFile.renameTo(new File(path));

            JSONObject jsonObject = new JSONObject();
            jsonObject.set("url", path);
            jsonObject.set("path", path);
            uploadResults.add(jsonObject);

            convFile.delete(); // 上传后删除临时文件
        }

        return R.ok(uploadResults);
    }

    @Log(title = "模型列表", businessType = BusinessType.API)
    @GetMapping("/models")
    public R<Object> models() {
        List<JSONObject> list = homeService.models();
        return R.ok(list);
    }

    @Log(title = "模型列表", businessType = BusinessType.API)
    @GetMapping("/modelList")
    public R<Object> modelList() {
        List<AiModels> list = homeService.modelList();
        for (AiModels aiModels : list) {
            aiModels.setIcon(aiConfig.getPreviewUrl() + "/static/avatar.png");
        }
        return R.ok(list);
    }

    @Log(title = "文件上传", businessType = BusinessType.API,isSaveRequestData = false)
    @PostMapping("/upload")
    public R<Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String path = FileUploadUtils.upload(file);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("url",aiConfig.getPreviewUrl() + path);
        jsonObject.set("path",path);
        return R.ok(jsonObject);
    }

    @Log(title = "多文件上传", businessType = BusinessType.API,isSaveRequestData = false)
    @PostMapping("/multipart-upload")
    public R<Object> multipartUpload(@RequestParam("files") MultipartFile[] files) throws IOException {
        JSONArray jsonArray = new JSONArray(); // 用于存储多个文件的上传结果

        for (MultipartFile file : files) {

            String path = FileUploadUtils.upload(file);

            JSONObject jsonObject = new JSONObject();
            jsonObject.set("url", path);
            jsonObject.set("path", path);
            jsonArray.add(jsonObject);

        }

        return R.ok(jsonArray);
    }
}
