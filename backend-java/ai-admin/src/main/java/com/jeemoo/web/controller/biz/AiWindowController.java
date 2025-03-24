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
import com.jeemoo.system.domain.vo.AiWindowVo;
import com.jeemoo.system.domain.bo.AiWindowBo;
import com.jeemoo.system.service.IAiWindowService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 窗口
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/window")
public class AiWindowController extends BaseController {

    private final IAiWindowService iAiWindowService;

    /**
     * 查询窗口列表
     */
    @SaCheckPermission("system:window:list")
    @GetMapping("/list")
    public TableDataInfo<AiWindowVo> list(AiWindowBo bo, PageQuery pageQuery) {
        return iAiWindowService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出窗口列表
     */
    @SaCheckPermission("system:window:export")
    @Log(title = "窗口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiWindowBo bo, HttpServletResponse response) {
        List<AiWindowVo> list = iAiWindowService.queryList(bo);
        ExcelUtil.exportExcel(list, "窗口", AiWindowVo.class, response);
    }

    /**
     * 获取窗口详细信息
     *
     * @param windowId 主键
     */
    @SaCheckPermission("system:window:query")
    @GetMapping("/{windowId}")
    public R<AiWindowVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long windowId) {
        return R.ok(iAiWindowService.queryById(windowId));
    }

    /**
     * 新增窗口
     */
    @SaCheckPermission("system:window:add")
    @Log(title = "窗口", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiWindowBo bo) {
        return toAjax(iAiWindowService.insertByBo(bo));
    }

    /**
     * 修改窗口
     */
    @SaCheckPermission("system:window:edit")
    @Log(title = "窗口", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiWindowBo bo) {
        return toAjax(iAiWindowService.updateByBo(bo));
    }

    /**
     * 删除窗口
     *
     * @param windowIds 主键串
     */
    @SaCheckPermission("system:window:remove")
    @Log(title = "窗口", businessType = BusinessType.DELETE)
    @DeleteMapping("/{windowIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] windowIds) {
        return toAjax(iAiWindowService.deleteWithValidByIds(Arrays.asList(windowIds), true));
    }
}
