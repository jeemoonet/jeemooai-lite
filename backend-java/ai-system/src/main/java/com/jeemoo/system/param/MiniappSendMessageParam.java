package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MiniappSendMessageParam {

    @NotEmpty(message = "customerUuid不能为空")
    private String customerUuid;

    @NotEmpty(message = "message消息不能为空")
    private String message;
}
