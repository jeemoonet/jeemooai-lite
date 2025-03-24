package com.jeemoo.system.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginParam {

    @NotEmpty(message = "手机号不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
