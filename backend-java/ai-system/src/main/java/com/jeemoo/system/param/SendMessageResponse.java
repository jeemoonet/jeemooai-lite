package com.jeemoo.system.param;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class SendMessageResponse {

    private Long windowId;

    private String windowName;

    private Long messageId;

    private Long promptId;

    private String promptDesc;

    private String promptIcon;

    private String promptName;

    private String masterUuid;

    private List<JSONObject> docInfo;

    private boolean needSearch;
}

