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
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.system.service.IAiDocumentCategoryService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 知识库分类
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/documentCategory")
public class AiDocumentCategoryController extends BaseController {

    private final IAiDocumentCategoryService iAiDocumentCategoryService;

    /**
     * 查询知识库分类列表
     */
    @SaCheckPermission("system:documentCategory:list")
    @GetMapping("/list")
    public TableDataInfo<AiDocumentCategoryVo> list(AiDocumentCategoryBo bo, PageQuery pageQuery) {
        return iAiDocumentCategoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出知识库分类列表
     */
    @SaCheckPermission("system:documentCategory:export")
    @Log(title = "知识库分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiDocumentCategoryBo bo, HttpServletResponse response) {
        List<AiDocumentCategoryVo> list = iAiDocumentCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "知识库分类", AiDocumentCategoryVo.class, response);
    }

    /**
     * 获取知识库分类详细信息
     *
     * @param categoryId 主键
     */
    @SaCheckPermission("system:documentCategory:query")
    @GetMapping("/{categoryId}")
    public R<AiDocumentCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long categoryId) {
        return R.ok(iAiDocumentCategoryService.queryById(categoryId));
    }

    /**
     * 新增知识库分类
     */
    @SaCheckPermission("system:documentCategory:add")
    @Log(title = "知识库分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiDocumentCategoryBo bo) {
        return toAjax(iAiDocumentCategoryService.insertByBo(bo));
    }

    /**
     * 修改知识库分类
     */
    @SaCheckPermission("system:documentCategory:edit")
    @Log(title = "知识库分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiDocumentCategoryBo bo) {
        return toAjax(iAiDocumentCategoryService.updateByBo(bo));
    }

    /**
     * 删除知识库分类
     *
     * @param categoryIds 主键串
     */
    @SaCheckPermission("system:documentCategory:remove")
    @Log(title = "知识库分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] categoryIds) {
        return toAjax(iAiDocumentCategoryService.deleteWithValidByIds(Arrays.asList(categoryIds), true));
    }
}
