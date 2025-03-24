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
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.domain.bo.AiCompanyBo;
import com.jeemoo.system.service.IAiCompanyService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 企业资料
 *
 * @author ai
 * @date 2023-09-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/company")
public class AiCompanyController extends BaseController {

    private final IAiCompanyService iAiCompanyService;

    /**
     * 查询企业资料列表
     */
    @SaCheckPermission("system:company:list")
    @GetMapping("/list")
    public TableDataInfo<AiCompanyVo> list(AiCompanyBo bo, PageQuery pageQuery) {
        return iAiCompanyService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("system:company:list")
    @GetMapping("/all")
    public List<AiCompanyVo> all(AiCompanyBo bo) {
        return iAiCompanyService.queryList(bo);
    }

    /**
     * 导出企业资料列表
     */
    @SaCheckPermission("system:company:export")
    @Log(title = "企业资料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiCompanyBo bo, HttpServletResponse response) {
        List<AiCompanyVo> list = iAiCompanyService.queryList(bo);
        ExcelUtil.exportExcel(list, "企业资料", AiCompanyVo.class, response);
    }

    /**
     * 获取企业资料详细信息
     *
     * @param companyId 主键
     */
    @SaCheckPermission("system:company:query")
    @GetMapping("/{companyId}")
    public R<AiCompanyVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long companyId) {
        return R.ok(iAiCompanyService.queryById(companyId));
    }

    /**
     * 新增企业资料
     */
    @SaCheckPermission("system:company:add")
    @Log(title = "企业资料", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiCompanyBo bo) {
        return toAjax(iAiCompanyService.insertByBo(bo));
    }

    /**
     * 修改企业资料
     */
    @SaCheckPermission("system:company:edit")
    @Log(title = "企业资料", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiCompanyBo bo) {
        return toAjax(iAiCompanyService.updateByBo(bo));
    }

    @SaCheckPermission("system:company:edit")
    @Log(title = "企业设置套餐", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/setPackage")
    public R<Void> setPackage(@RequestBody AiCompanyBo bo) {
        return toAjax(iAiCompanyService.updateByBo(bo));
    }

    /**
     * 删除企业资料
     *
     * @param companyIds 主键串
     */
    @SaCheckPermission("system:company:remove")
    @Log(title = "企业资料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{companyIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] companyIds) {
        return toAjax(iAiCompanyService.deleteWithValidByIds(Arrays.asList(companyIds), true));
    }

    @PostMapping("/updateRemark")
    public R<Void> updateRemark(@RequestBody AiCompanyBo bo) {
        return toAjax(iAiCompanyService.updateRemark(bo));
    }
}
