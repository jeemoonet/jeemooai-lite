package com.jeemoo.system.param;

import cn.hutool.json.JSONObject;
import lombok.Data;

@Data
public class SpeakerEvent {
    
    private String event;
    
    private SpeakerEventParam data;

    private JSONObject tempData;
}
