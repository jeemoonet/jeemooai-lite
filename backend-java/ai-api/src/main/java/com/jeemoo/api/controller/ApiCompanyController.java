package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.json.JSONObject;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.validate.ApiEditGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.system.domain.bo.AiCompanyBo;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.param.CompanyPromptResponse;
import com.jeemoo.system.param.PromptSearchParam;
import com.jeemoo.system.service.IAiCompanyService;
import com.jeemoo.system.service.IAiPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/company")
public class ApiCompanyController {

    private final IAiCompanyService companyService;
    private final IAiPromptService promptService;

    @Log(title = "获取企业资料", businessType = BusinessType.API)
    @GetMapping("/getInfo")
    public R<Object> getInfo() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiCompanyVo companyVo = companyService.queryById(loginUser.getCompanyId());
        if (companyVo == null) {
            return R.fail("企业不存在");
        }

        return R.ok(companyVo);
    }

    @Log(title = "设置企业资料", businessType = BusinessType.API)
    @PostMapping("/updateInfo")
    public R<Object> updateInfo(@RequestBody @Validated(ApiEditGroup.class) AiCompanyBo companyBo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        companyBo.setUserId(null);
        companyBo.setUserCount(null);
        companyBo.setUserCountMax(null);
        companyBo.setIntegral(null);
        companyBo.setIntegralTotal(null);
        companyBo.setDocumentWordCount(null);
        companyBo.setDocumentWordCountMax(null);
        companyBo.setIsTrial(null);
        companyBo.setAdvancedPackage(null);
        companyBo.setAdvancedPackageId(null);
        companyBo.setAdvancedPackageLevel(null);
        companyBo.setCompanyId(loginUser.getCompanyId());
        if (companyService.updateByBo(companyBo)) {
            companyService.updateUserCompanyName(loginUser.getUserId(),companyBo.getCompanyName());
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "企业提示器列表", businessType = BusinessType.API)
    @GetMapping("/promptPage")
    public TableDataInfo<CompanyPromptResponse> promptPage(PromptSearchParam param, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        param.setCompanyId(loginUser.getCompanyId());
        return promptService.companyPromptPage(param,pageQuery);
    }

}
