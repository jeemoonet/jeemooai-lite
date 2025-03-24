package com.jeemoo.system.param;

import cn.hutool.json.JSONObject;
import lombok.Data;

@Data
public class CommonEvent {

    private String event;

    private JSONObject data;
}
