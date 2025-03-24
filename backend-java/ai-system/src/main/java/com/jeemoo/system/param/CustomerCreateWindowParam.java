package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustomerCreateWindowParam {

    @NotEmpty(message = "customerUuid不能为空")
    private String customerUuid;
}
