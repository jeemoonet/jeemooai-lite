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
import com.jeemoo.system.domain.vo.AiPromptTipsVo;
import com.jeemoo.system.domain.bo.AiPromptTipsBo;
import com.jeemoo.system.service.IAiPromptTipsService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 提示器常用语
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/promptTips")
public class AiPromptTipsController extends BaseController {

    private final IAiPromptTipsService iAiPromptTipsService;

    /**
     * 查询提示器常用语列表
     */
    @SaCheckPermission("system:promptTips:list")
    @GetMapping("/list")
    public TableDataInfo<AiPromptTipsVo> list(AiPromptTipsBo bo, PageQuery pageQuery) {
        return iAiPromptTipsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出提示器常用语列表
     */
    @SaCheckPermission("system:promptTips:export")
    @Log(title = "提示器常用语", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiPromptTipsBo bo, HttpServletResponse response) {
        List<AiPromptTipsVo> list = iAiPromptTipsService.queryList(bo);
        ExcelUtil.exportExcel(list, "提示器常用语", AiPromptTipsVo.class, response);
    }

    /**
     * 获取提示器常用语详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:promptTips:query")
    @GetMapping("/{id}")
    public R<AiPromptTipsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iAiPromptTipsService.queryById(id));
    }

    /**
     * 新增提示器常用语
     */
    @SaCheckPermission("system:promptTips:add")
    @Log(title = "提示器常用语", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiPromptTipsBo bo) {
        return toAjax(iAiPromptTipsService.insertByBo(bo));
    }

    /**
     * 修改提示器常用语
     */
    @SaCheckPermission("system:promptTips:edit")
    @Log(title = "提示器常用语", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiPromptTipsBo bo) {
        return toAjax(iAiPromptTipsService.updateByBo(bo));
    }

    /**
     * 删除提示器常用语
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:promptTips:remove")
    @Log(title = "提示器常用语", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAiPromptTipsService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
