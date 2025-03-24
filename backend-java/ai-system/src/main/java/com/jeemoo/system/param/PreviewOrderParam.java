package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PreviewOrderParam {

    @NotNull(message = "type不能为空")
    private Integer type;

    @NotNull(message = "packageId不能为空")
    private Long packageId;

    private Integer year;
}
