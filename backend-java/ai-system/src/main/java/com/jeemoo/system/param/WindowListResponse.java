package com.jeemoo.system.param;

import lombok.Data;

@Data
public class WindowListResponse {

    private String windowName;

    private Long windowId;

    private Long promptId;

    private String promptDesc;

    private String promptIcon;

    private String promptName;

    private Integer  isContext;
}
