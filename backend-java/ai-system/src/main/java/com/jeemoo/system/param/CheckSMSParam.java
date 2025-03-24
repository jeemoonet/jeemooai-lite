package com.jeemoo.system.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class CheckSMSParam {

    @NotEmpty(message = "手机号不能为空")
    @Length(max = 255,message = "手机号长度不能超过255个字符")
    private String userName;

    private String smsCode;
}
