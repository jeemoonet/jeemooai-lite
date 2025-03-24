package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SmsLoginParam {

    @NotEmpty(message = "手机号不能为空")
    private String userName;

    @NotEmpty(message = "短信验证码不能为空")
    private String code;
}
