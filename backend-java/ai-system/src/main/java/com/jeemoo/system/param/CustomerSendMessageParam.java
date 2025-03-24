package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CustomerSendMessageParam extends SendMessageParam{

    @NotEmpty(message = "customerUuid不能为空")
    private String customerUuid;
}
