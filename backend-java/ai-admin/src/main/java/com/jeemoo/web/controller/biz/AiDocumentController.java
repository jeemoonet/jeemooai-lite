package com.jeemoo.web.controller.biz;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.jeemoo.common.annotation.RepeatSubmit;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.controller.BaseController;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import com.jeemoo.common.core.validate.QueryGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.utils.poi.ExcelUtil;
import com.jeemoo.system.domain.vo.AiDocumentVo;
import com.jeemoo.system.domain.bo.AiDocumentBo;
import com.jeemoo.system.service.IAiDocumentService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 文档
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/document")
public class AiDocumentController extends BaseController {

    private final IAiDocumentService iAiDocumentService;

    /**
     * 查询文档列表
     */
    @SaCheckPermission("system:document:list")
    @GetMapping("/list")
    public TableDataInfo<AiDocumentVo> list(AiDocumentBo bo, PageQuery pageQuery) {
        return iAiDocumentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出文档列表
     */
    @SaCheckPermission("system:document:export")
    @Log(title = "文档", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiDocumentBo bo, HttpServletResponse response) {
        List<AiDocumentVo> list = iAiDocumentService.queryList(bo);
        ExcelUtil.exportExcel(list, "文档", AiDocumentVo.class, response);
    }

    /**
     * 获取文档详细信息
     *
     * @param documentId 主键
     */
    @SaCheckPermission("system:document:query")
    @GetMapping("/{documentId}")
    public R<AiDocumentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long documentId) {
        return R.ok(iAiDocumentService.queryById(documentId));
    }

    /**
     * 新增文档
     */
    @SaCheckPermission("system:document:add")
    @Log(title = "文档", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiDocumentBo bo) {
        return toAjax(iAiDocumentService.insertByBo(bo));
    }

    /**
     * 修改文档
     */
    @SaCheckPermission("system:document:edit")
    @Log(title = "文档", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiDocumentBo bo) {
        return toAjax(iAiDocumentService.updateByBo(bo));
    }

    /**
     * 删除文档
     *
     * @param documentIds 主键串
     */
    @SaCheckPermission("system:document:remove")
    @Log(title = "文档", businessType = BusinessType.DELETE)
    @DeleteMapping("/{documentIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] documentIds) {
        return toAjax(iAiDocumentService.deleteWithValidByIds(Arrays.asList(documentIds), true));
    }
}
