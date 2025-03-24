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
import com.jeemoo.system.domain.vo.AiRecordLikeVo;
import com.jeemoo.system.domain.bo.AiRecordLikeBo;
import com.jeemoo.system.service.IAiRecordLikeService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 聊天记录点赞不喜欢
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/recordLike")
public class AiRecordLikeController extends BaseController {

    private final IAiRecordLikeService iAiRecordLikeService;

    /**
     * 查询聊天记录点赞不喜欢列表
     */
    @SaCheckPermission("system:recordLike:list")
    @GetMapping("/list")
    public TableDataInfo<AiRecordLikeVo> list(AiRecordLikeBo bo, PageQuery pageQuery) {
        return iAiRecordLikeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出聊天记录点赞不喜欢列表
     */
    @SaCheckPermission("system:recordLike:export")
    @Log(title = "聊天记录点赞不喜欢", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiRecordLikeBo bo, HttpServletResponse response) {
        List<AiRecordLikeVo> list = iAiRecordLikeService.queryList(bo);
        ExcelUtil.exportExcel(list, "聊天记录点赞不喜欢", AiRecordLikeVo.class, response);
    }

    /**
     * 获取聊天记录点赞不喜欢详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:recordLike:query")
    @GetMapping("/{id}")
    public R<AiRecordLikeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iAiRecordLikeService.queryById(id));
    }

    /**
     * 新增聊天记录点赞不喜欢
     */
    @SaCheckPermission("system:recordLike:add")
    @Log(title = "聊天记录点赞不喜欢", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiRecordLikeBo bo) {
        return toAjax(iAiRecordLikeService.insertByBo(bo));
    }

    /**
     * 修改聊天记录点赞不喜欢
     */
    @SaCheckPermission("system:recordLike:edit")
    @Log(title = "聊天记录点赞不喜欢", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiRecordLikeBo bo) {
        return toAjax(iAiRecordLikeService.updateByBo(bo));
    }

    /**
     * 删除聊天记录点赞不喜欢
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:recordLike:remove")
    @Log(title = "聊天记录点赞不喜欢", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAiRecordLikeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
