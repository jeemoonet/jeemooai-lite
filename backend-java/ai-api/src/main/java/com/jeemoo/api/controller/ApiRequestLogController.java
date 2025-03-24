package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.json.JSONObject;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.bo.AiRequestLogBo;
import com.jeemoo.system.domain.vo.AiRequestLogVo;
import com.jeemoo.system.param.PromptSearchParam;
import com.jeemoo.system.service.IAiRequestLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requestLog")
@RequiredArgsConstructor
public class ApiRequestLogController {

    private final IAiRequestLogService requestLogService;

    @Log(title = "聊天记录列表")
    @GetMapping("/page")
    public TableDataInfo<AiRequestLogVo> page(AiRequestLogBo param, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        param.setCompanyId(loginUser.getCompanyId());
        return requestLogService.page(param,pageQuery);
    }

    @Log(title = "聊天记录详情")
    @GetMapping("/info")
    public R<Object> info(String uuid) {
        AiRequestLogVo requestLogVo = requestLogService.queryById(uuid);
        if (requestLogVo == null) {
            return R.fail("记录不存在");
        }
        JSONObject result = new JSONObject();
        result.set("question",requestLogVo.getQuestion()==null?"":requestLogVo.getQuestion());
        result.set("receiveMessage",requestLogVo.getReceiveMessage()==null?"":requestLogVo.getReceiveMessage());
        result.set("errorMessage",requestLogVo.getErrorMessage()==null?"":requestLogVo.getErrorMessage());

        return R.ok(result);
    }
}
