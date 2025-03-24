package com.jeemoo.system.param;

import cn.hutool.json.JSONObject;
import lombok.Data;

@Data
public class WrokflowLogResponse {

    private String nodeId;

    private String nodeType;

    private String nodeTitle;

    private String error;

    private JSONObject inputs;

    private JSONObject outputs;
}
