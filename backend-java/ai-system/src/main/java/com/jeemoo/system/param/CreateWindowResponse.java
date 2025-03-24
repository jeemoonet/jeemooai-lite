package com.jeemoo.system.param;

import lombok.Data;

@Data
public class CreateWindowResponse {

    private Long windowId;

    private String windowName;

    private String initMessage;

    private Long promptId;

    private String promptDesc;

    private String promptIcon;

    private String promptName;

    private Long messageId;

    private Integer isContext;

    private Long digitPersonId;
}
