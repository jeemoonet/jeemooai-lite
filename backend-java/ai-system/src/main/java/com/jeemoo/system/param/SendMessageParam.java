package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SendMessageParam {

    public Long windowId;

    @NotEmpty(message = "消息内容不能为空")
    public String message;

    public Integer isContext = 1;

    public List<String> filesUuid;

    public String platform = "pc";

    public Integer isNeedSearch = 0;

    public Integer isReasoning = 0;
}
