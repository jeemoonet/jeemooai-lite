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
import com.jeemoo.system.domain.vo.AiPromptDocumentVo;
import com.jeemoo.system.domain.bo.AiPromptDocumentBo;
import com.jeemoo.system.service.IAiPromptDocumentService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 提示器文档
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/promptDocument")
public class AiPromptDocumentController extends BaseController {

    private final IAiPromptDocumentService iAiPromptDocumentService;

    /**
     * 查询提示器文档列表
     */
    @SaCheckPermission("system:promptDocument:list")
    @GetMapping("/list")
    public TableDataInfo<AiPromptDocumentVo> list(AiPromptDocumentBo bo, PageQuery pageQuery) {
        return iAiPromptDocumentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出提示器文档列表
     */
    @SaCheckPermission("system:promptDocument:export")
    @Log(title = "提示器文档", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiPromptDocumentBo bo, HttpServletResponse response) {
        List<AiPromptDocumentVo> list = iAiPromptDocumentService.queryList(bo);
        ExcelUtil.exportExcel(list, "提示器文档", AiPromptDocumentVo.class, response);
    }

    /**
     * 获取提示器文档详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:promptDocument:query")
    @GetMapping("/{id}")
    public R<AiPromptDocumentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iAiPromptDocumentService.queryById(id));
    }

    /**
     * 新增提示器文档
     */
    @SaCheckPermission("system:promptDocument:add")
    @Log(title = "提示器文档", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiPromptDocumentBo bo) {
        return toAjax(iAiPromptDocumentService.insertByBo(bo));
    }

    /**
     * 修改提示器文档
     */
    @SaCheckPermission("system:promptDocument:edit")
    @Log(title = "提示器文档", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiPromptDocumentBo bo) {
        return toAjax(iAiPromptDocumentService.updateByBo(bo));
    }

    /**
     * 删除提示器文档
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:promptDocument:remove")
    @Log(title = "提示器文档", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAiPromptDocumentService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
