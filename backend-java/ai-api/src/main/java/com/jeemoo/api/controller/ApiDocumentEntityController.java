package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.dtflys.forest.annotation.Post;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.bo.AiDocumentBo;
import com.jeemoo.system.domain.vo.AiDocumentVo;
import com.jeemoo.system.param.VectorData;
import com.jeemoo.system.param.VectorParam;
import com.jeemoo.system.service.IAiCompanyService;
import com.jeemoo.system.service.IAiDocumentService;
import com.jeemoo.system.service.VectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/documentEntity")
@RequiredArgsConstructor
public class ApiDocumentEntityController {

    private final VectorService vectorService;
    private final IAiDocumentService documentService;
    private final IAiCompanyService companyService;

    @Log(title = "向量分页列表", businessType = BusinessType.API)
    @GetMapping("/page")
    public TableDataInfo<VectorData> page(int pageNum, int pageSize, Long documentId, String keywords) throws IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return vectorService.listPage("company_"+loginUser.getCompanyId(),documentId,pageNum,pageSize,keywords);
    }

    @Log(title = "向量新增", businessType = BusinessType.API)
    @PostMapping("/create")
    @Transactional
    public R<Object> create(@Validated @RequestBody VectorParam param) throws IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentVo document = documentService.queryById(param.getDocumentId());
        if (!Objects.equals(document.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail("不可操作此文档");
        }

        int byteLength = param.getContent().getBytes(StandardCharsets.UTF_8).length;
        if (byteLength > 4000) {
            return R.fail("内容字节数量不能超过4000,当前数量为："+byteLength);
        }

        if (vectorService.insertVector("company_"+loginUser.getCompanyId(),
                new VectorData(null,
                        param.getContent(),
                        (long)param.getContent().length(),
                        document.getCategoryId(),
                        document.getDocumentId(),
                        0))) {
            AiDocumentBo documentBo = new AiDocumentBo();
            documentBo.setCompanyId(loginUser.getCompanyId());
            documentBo.setFileSize((long) param.getContent().length());
            return R.ok();
        } else {
            return R.fail("创建失败");
        }
    }

    @Log(title = "向量修改", businessType = BusinessType.API)
    @PostMapping("/update")
    @Transactional
    public R<Object> update(@Validated @RequestBody VectorParam param) throws IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentVo document = documentService.queryById(param.getDocumentId());
        if (!Objects.equals(document.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail("不可操作此文档");
        }

        int byteLength = param.getContent().getBytes(StandardCharsets.UTF_8).length;
        if (byteLength > 4000) {
            return R.fail("内容字节数量不能超过4000,当前数量为："+byteLength);
        }

        VectorData oldVectorData = vectorService.getVector("company_"+loginUser.getCompanyId(),param.getId());
        if (oldVectorData == null) {
            oldVectorData = new VectorData(null,"",0L,0L,0L,0);
        }

        if (vectorService.updateVector("company_"+loginUser.getCompanyId(),
                new VectorData(param.getId(),
                        param.getContent(),
                        (long)param.getContent().length(),
                        document.getCategoryId(),
                        document.getDocumentId(),
                        0))) {

            //减容量
            document.setFileSize(oldVectorData.getContentWordCount());

            //加容量
            AiDocumentBo documentBo = new AiDocumentBo();
            documentBo.setCompanyId(loginUser.getCompanyId());
            documentBo.setFileSize((long) param.getContent().length());
            return R.ok("更新成功");
        } else {
            return R.fail("更新失败");
        }
    }

    @Log(title = "向量删除", businessType = BusinessType.API)
    @PostMapping("/delete")
    @Transactional
    public R<Object> delete(@RequestBody JSONObject param) throws IOException {
        Long documentId = param.getLong("documentId");
        List<String> ids = param.getBeanList("ids", String.class);
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentVo document = documentService.queryById(documentId);
        if (!Objects.equals(document.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail("不可操作此数据");
        }
        List<VectorData> list = vectorService.listByIds("company_"+loginUser.getCompanyId(), ids);
        int sumCount = 0;
        for (VectorData vectorData : list) {
            sumCount += vectorData.getContentWordCount();
        }
        vectorService.deleteVector("company_"+loginUser.getCompanyId(), ids);
        document.setFileSize((long) sumCount);
        return R.ok("删除成功");
    }
}
