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
import com.jeemoo.system.domain.vo.AiDocumentEntityVo;
import com.jeemoo.system.domain.bo.AiDocumentEntityBo;
import com.jeemoo.system.service.IAiDocumentEntityService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 文档向量关系
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/documentEntity")
public class AiDocumentEntityController extends BaseController {

    private final IAiDocumentEntityService iAiDocumentEntityService;

    /**
     * 查询文档向量关系列表
     */
    @SaCheckPermission("system:documentEntity:list")
    @GetMapping("/list")
    public TableDataInfo<AiDocumentEntityVo> list(AiDocumentEntityBo bo, PageQuery pageQuery) {
        return iAiDocumentEntityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出文档向量关系列表
     */
    @SaCheckPermission("system:documentEntity:export")
    @Log(title = "文档向量关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiDocumentEntityBo bo, HttpServletResponse response) {
        List<AiDocumentEntityVo> list = iAiDocumentEntityService.queryList(bo);
        ExcelUtil.exportExcel(list, "文档向量关系", AiDocumentEntityVo.class, response);
    }

    /**
     * 获取文档向量关系详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:documentEntity:query")
    @GetMapping("/{id}")
    public R<AiDocumentEntityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iAiDocumentEntityService.queryById(id));
    }

    /**
     * 新增文档向量关系
     */
    @SaCheckPermission("system:documentEntity:add")
    @Log(title = "文档向量关系", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiDocumentEntityBo bo) {
        return toAjax(iAiDocumentEntityService.insertByBo(bo));
    }

    /**
     * 修改文档向量关系
     */
    @SaCheckPermission("system:documentEntity:edit")
    @Log(title = "文档向量关系", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiDocumentEntityBo bo) {
        return toAjax(iAiDocumentEntityService.updateByBo(bo));
    }

    /**
     * 删除文档向量关系
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:documentEntity:remove")
    @Log(title = "文档向量关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAiDocumentEntityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
