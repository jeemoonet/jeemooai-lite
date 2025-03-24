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
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.domain.bo.AiPromptCategoryBo;
import com.jeemoo.system.service.IAiPromptCategoryService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 提示器分类
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/promptCategory")
public class AiPromptCategoryController extends BaseController {

    private final IAiPromptCategoryService iAiPromptCategoryService;

    /**
     * 查询提示器分类列表
     */
    @SaCheckPermission("system:promptCategory:list")
    @GetMapping("/list")
    public TableDataInfo<AiPromptCategoryVo> list(AiPromptCategoryBo bo, PageQuery pageQuery) {
        return iAiPromptCategoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出提示器分类列表
     */
    @SaCheckPermission("system:promptCategory:export")
    @Log(title = "提示器分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiPromptCategoryBo bo, HttpServletResponse response) {
        List<AiPromptCategoryVo> list = iAiPromptCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "提示器分类", AiPromptCategoryVo.class, response);
    }

    /**
     * 获取提示器分类详细信息
     *
     * @param categoryId 主键
     */
    @SaCheckPermission("system:promptCategory:query")
    @GetMapping("/{categoryId}")
    public R<AiPromptCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long categoryId) {
        return R.ok(iAiPromptCategoryService.queryById(categoryId));
    }

    /**
     * 新增提示器分类
     */
    @SaCheckPermission("system:promptCategory:add")
    @Log(title = "提示器分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiPromptCategoryBo bo) {
        return toAjax(iAiPromptCategoryService.insertByBo(bo));
    }

    /**
     * 修改提示器分类
     */
    @SaCheckPermission("system:promptCategory:edit")
    @Log(title = "提示器分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiPromptCategoryBo bo) {
        return toAjax(iAiPromptCategoryService.updateByBo(bo));
    }

    /**
     * 删除提示器分类
     *
     * @param categoryIds 主键串
     */
    @SaCheckPermission("system:promptCategory:remove")
    @Log(title = "提示器分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] categoryIds) {
        return toAjax(iAiPromptCategoryService.deleteWithValidByIds(Arrays.asList(categoryIds), true));
    }
}
