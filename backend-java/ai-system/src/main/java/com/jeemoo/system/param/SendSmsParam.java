package com.jeemoo.system.param;

import com.jeemoo.common.core.validate.LoginGroup;
import com.jeemoo.common.core.validate.RegisterGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SendSmsParam {

    @NotEmpty(message = "手机号不能为空", groups = {LoginGroup.class, RegisterGroup.class})
    private String phone;

    private String uuid;

    @NotEmpty(message = "scope不能为空", groups = {LoginGroup.class, RegisterGroup.class})
    private String scope;

    private String code;
}
