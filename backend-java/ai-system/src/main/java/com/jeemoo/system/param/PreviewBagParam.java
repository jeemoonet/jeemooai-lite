package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PreviewBagParam {

    @NotEmpty(message = "type不能为空")
    private String type;
    @NotNull(message = "num不能为空")
    private Integer num;
}
