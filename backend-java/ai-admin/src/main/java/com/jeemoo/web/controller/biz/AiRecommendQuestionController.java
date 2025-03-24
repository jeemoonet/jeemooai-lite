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
import com.jeemoo.system.domain.vo.AiRecommendQuestionVo;
import com.jeemoo.system.domain.bo.AiRecommendQuestionBo;
import com.jeemoo.system.service.IAiRecommendQuestionService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 推荐问题
 *
 * @author ai
 * @date 2024-05-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/recommendQuestion")
public class AiRecommendQuestionController extends BaseController {

    private final IAiRecommendQuestionService iAiRecommendQuestionService;

    /**
     * 查询推荐问题列表
     */
    @SaCheckPermission("system:recommendQuestion:list")
    @GetMapping("/list")
    public TableDataInfo<AiRecommendQuestionVo> list(AiRecommendQuestionBo bo, PageQuery pageQuery) {
        return iAiRecommendQuestionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出推荐问题列表
     */
    @SaCheckPermission("system:recommendQuestion:export")
    @Log(title = "推荐问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiRecommendQuestionBo bo, HttpServletResponse response) {
        List<AiRecommendQuestionVo> list = iAiRecommendQuestionService.queryList(bo);
        ExcelUtil.exportExcel(list, "推荐问题", AiRecommendQuestionVo.class, response);
    }

    /**
     * 获取推荐问题详细信息
     *
     * @param questionId 主键
     */
    @SaCheckPermission("system:recommendQuestion:query")
    @GetMapping("/{questionId}")
    public R<AiRecommendQuestionVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long questionId) {
        return R.ok(iAiRecommendQuestionService.queryById(questionId));
    }

    /**
     * 新增推荐问题
     */
    @SaCheckPermission("system:recommendQuestion:add")
    @Log(title = "推荐问题", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiRecommendQuestionBo bo) {
        return toAjax(iAiRecommendQuestionService.insertByBo(bo));
    }

    /**
     * 修改推荐问题
     */
    @SaCheckPermission("system:recommendQuestion:edit")
    @Log(title = "推荐问题", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiRecommendQuestionBo bo) {
        return toAjax(iAiRecommendQuestionService.updateByBo(bo));
    }

    /**
     * 删除推荐问题
     *
     * @param questionIds 主键串
     */
    @SaCheckPermission("system:recommendQuestion:remove")
    @Log(title = "推荐问题", businessType = BusinessType.DELETE)
    @DeleteMapping("/{questionIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] questionIds) {
        return toAjax(iAiRecommendQuestionService.deleteWithValidByIds(Arrays.asList(questionIds), true));
    }
}
