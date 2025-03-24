package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SendTestMessageParam {

    @NotEmpty(message = "消息内容不能为空")
    private String message;

    private String uuid;

    private Long promptId;

    private List<String> models;

    private List<String> filesUuid;
}
