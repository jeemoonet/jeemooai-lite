package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RenameWindowParam {

    @NotEmpty(message = "会话名称不能为空")
    private String windowName;

    @NotNull(message = "会话id不能为空")
    private Long windowId;
}
