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
import com.jeemoo.system.domain.vo.AiRequestLogVo;
import com.jeemoo.system.domain.bo.AiRequestLogBo;
import com.jeemoo.system.service.IAiRequestLogService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 请求记录
 *
 * @author ai
 * @date 2023-09-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/requestLog")
public class AiRequestLogController extends BaseController {

    private final IAiRequestLogService iAiRequestLogService;

    /**
     * 查询请求记录列表
     */
    @SaCheckPermission("system:requestLog:list")
    @GetMapping("/list")
    public TableDataInfo<AiRequestLogVo> list(AiRequestLogBo bo, PageQuery pageQuery) {
        return iAiRequestLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出请求记录列表
     */
    @SaCheckPermission("system:requestLog:export")
    @Log(title = "请求记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiRequestLogBo bo, HttpServletResponse response) {
        List<AiRequestLogVo> list = iAiRequestLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "请求记录", AiRequestLogVo.class, response);
    }

    /**
     * 获取请求记录详细信息
     *
     * @param uuid 主键
     */
    @SaCheckPermission("system:requestLog:query")
    @GetMapping("/{uuid}")
    public R<AiRequestLogVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String uuid) {
        return R.ok(iAiRequestLogService.queryById(uuid));
    }

    /**
     * 新增请求记录
     */
    @SaCheckPermission("system:requestLog:add")
    @Log(title = "请求记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiRequestLogBo bo) {
        return toAjax(iAiRequestLogService.insertByBo(bo));
    }

    /**
     * 修改请求记录
     */
    @SaCheckPermission("system:requestLog:edit")
    @Log(title = "请求记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiRequestLogBo bo) {
        return toAjax(iAiRequestLogService.updateByBo(bo));
    }

    /**
     * 删除请求记录
     *
     * @param uuids 主键串
     */
    @SaCheckPermission("system:requestLog:remove")
    @Log(title = "请求记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uuids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] uuids) {
        return toAjax(iAiRequestLogService.deleteWithValidByIds(Arrays.asList(uuids), true));
    }
}
