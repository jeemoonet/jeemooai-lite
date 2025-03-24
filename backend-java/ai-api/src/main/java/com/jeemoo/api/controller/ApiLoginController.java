package com.jeemoo.api.controller;


import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.jeemoo.system.param.*;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.constant.CacheConstants;
import com.jeemoo.common.constant.Constants;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.enums.CaptchaType;
import com.jeemoo.common.enums.UserType;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.utils.redis.RedisUtils;
import com.jeemoo.common.utils.reflect.ReflectUtils;
import com.jeemoo.common.utils.spring.SpringUtils;
import com.jeemoo.framework.config.properties.CaptchaProperties;
import com.jeemoo.system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class ApiLoginController {

    private final CaptchaProperties captchaProperties;
    private final ISysConfigService configService;
    private final SysLoginService loginService;


    @Value("${spring.profiles.active}")
    private String active;

    @GetMapping("/captchaImage")
    public R<Object> captchaImage() {
        Map<String, Object> ajax = new HashMap<>();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return R.ok(ajax);
        }
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        ajax.put("uuid", uuid);
        ajax.put("img", captcha.getImageBase64());
        return R.ok(ajax);
    }

    @Log(title = "密码登陆", businessType = BusinessType.API)
    @PostMapping("/password")
    public R<Object> password(@RequestBody @Validated LoginParam param) {
        String token = loginService.login(param.getUserName(), param.getPassword(), UserType.APP_USER.getUserType());
        JSONObject response = new JSONObject();
        response.set("token", "Bearer " + token);

        return R.ok(response);
    }


    @GetMapping("/healthCheck")
    public R<Object> healthCheck() {
        return R.ok();
    }
}
