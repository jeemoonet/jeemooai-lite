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
import com.jeemoo.system.domain.vo.AiSplitRuleVo;
import com.jeemoo.system.domain.bo.AiSplitRuleBo;
import com.jeemoo.system.service.IAiSplitRuleService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 拆分策略
 *
 * @author ai
 * @date 2023-10-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/splitRule")
public class AiSplitRuleController extends BaseController {

    private final IAiSplitRuleService iAiSplitRuleService;

    /**
     * 查询拆分策略列表
     */
    @SaCheckPermission("system:splitRule:list")
    @GetMapping("/list")
    public TableDataInfo<AiSplitRuleVo> list(AiSplitRuleBo bo, PageQuery pageQuery) {
        return iAiSplitRuleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出拆分策略列表
     */
    @SaCheckPermission("system:splitRule:export")
    @Log(title = "拆分策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiSplitRuleBo bo, HttpServletResponse response) {
        List<AiSplitRuleVo> list = iAiSplitRuleService.queryList(bo);
        ExcelUtil.exportExcel(list, "拆分策略", AiSplitRuleVo.class, response);
    }

    /**
     * 获取拆分策略详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:splitRule:query")
    @GetMapping("/{id}")
    public R<AiSplitRuleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Integer id) {
        return R.ok(iAiSplitRuleService.queryById(id));
    }

    /**
     * 新增拆分策略
     */
    @SaCheckPermission("system:splitRule:add")
    @Log(title = "拆分策略", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiSplitRuleBo bo) {
        return toAjax(iAiSplitRuleService.insertByBo(bo));
    }

    /**
     * 修改拆分策略
     */
    @SaCheckPermission("system:splitRule:edit")
    @Log(title = "拆分策略", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiSplitRuleBo bo) {
        return toAjax(iAiSplitRuleService.updateByBo(bo));
    }

    /**
     * 删除拆分策略
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:splitRule:remove")
    @Log(title = "拆分策略", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Integer[] ids) {
        return toAjax(iAiSplitRuleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
