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
import com.jeemoo.system.domain.vo.AiRecordVo;
import com.jeemoo.system.domain.bo.AiRecordBo;
import com.jeemoo.system.service.IAiRecordService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 聊天记录
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/record")
public class AiRecordController extends BaseController {

    private final IAiRecordService iAiRecordService;

    /**
     * 查询聊天记录列表
     */
    @SaCheckPermission("system:record:list")
    @GetMapping("/list")
    public TableDataInfo<AiRecordVo> list(AiRecordBo bo, PageQuery pageQuery) {
        return iAiRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出聊天记录列表
     */
    @SaCheckPermission("system:record:export")
    @Log(title = "聊天记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiRecordBo bo, HttpServletResponse response) {
        List<AiRecordVo> list = iAiRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "聊天记录", AiRecordVo.class, response);
    }

    /**
     * 获取聊天记录详细信息
     *
     * @param messageId 主键
     */
    @SaCheckPermission("system:record:query")
    @GetMapping("/{messageId}")
    public R<AiRecordVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long messageId) {
        return R.ok(iAiRecordService.queryById(messageId));
    }

    /**
     * 新增聊天记录
     */
    @SaCheckPermission("system:record:add")
    @Log(title = "聊天记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiRecordBo bo) {
        return toAjax(iAiRecordService.insertByBo(bo));
    }

    /**
     * 修改聊天记录
     */
    @SaCheckPermission("system:record:edit")
    @Log(title = "聊天记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiRecordBo bo) {
        return toAjax(iAiRecordService.updateByBo(bo));
    }

    /**
     * 删除聊天记录
     *
     * @param messageIds 主键串
     */
    @SaCheckPermission("system:record:remove")
    @Log(title = "聊天记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] messageIds) {
        return toAjax(iAiRecordService.deleteWithValidByIds(Arrays.asList(messageIds), true));
    }
}
