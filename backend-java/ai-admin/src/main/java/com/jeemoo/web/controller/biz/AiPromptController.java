package com.jeemoo.web.controller.biz;

import java.util.List;
import java.util.Arrays;

import cn.hutool.json.JSONObject;
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
import com.jeemoo.system.domain.vo.AiPromptVo;
import com.jeemoo.system.domain.bo.AiPromptBo;
import com.jeemoo.system.service.IAiPromptService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 提示器
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/prompt")
public class AiPromptController extends BaseController {

    private final IAiPromptService iAiPromptService;

    /**
     * 查询提示器列表
     */
    @SaCheckPermission("system:prompt:list")
    @GetMapping("/list")
    public TableDataInfo<AiPromptVo> list(AiPromptBo bo, PageQuery pageQuery) {
        return iAiPromptService.queryPageList(bo, pageQuery);
    }

    @GetMapping("/all")
    public List<JSONObject> all(AiPromptBo bo) {
        return iAiPromptService.options(bo);
    }

    /**
     * 导出提示器列表
     */
    @SaCheckPermission("system:prompt:export")
    @Log(title = "提示器", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiPromptBo bo, HttpServletResponse response) {
        List<AiPromptVo> list = iAiPromptService.queryList(bo);
        ExcelUtil.exportExcel(list, "提示器", AiPromptVo.class, response);
    }

    /**
     * 获取提示器详细信息
     *
     * @param promptId 主键
     */
    @SaCheckPermission("system:prompt:query")
    @GetMapping("/{promptId}")
    public R<AiPromptVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long promptId) {
        return R.ok(iAiPromptService.queryById(promptId));
    }

    /**
     * 新增提示器
     */
    @SaCheckPermission("system:prompt:add")
    @Log(title = "提示器", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiPromptBo bo) {
        return toAjax(iAiPromptService.insertByBo(bo));
    }

    /**
     * 修改提示器
     */
    @SaCheckPermission("system:prompt:edit")
    @Log(title = "提示器", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiPromptBo bo) {
        return toAjax(iAiPromptService.updateByBo(bo));
    }

    /**
     * 删除提示器
     *
     * @param promptIds 主键串
     */
    @SaCheckPermission("system:prompt:remove")
    @Log(title = "提示器", businessType = BusinessType.DELETE)
    @DeleteMapping("/{promptIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] promptIds) {
        return toAjax(iAiPromptService.deleteWithValidByIds(Arrays.asList(promptIds), true));
    }
}
