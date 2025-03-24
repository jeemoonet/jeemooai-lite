package com.jeemoo.system.param;

import lombok.Data;

@Data
public class SpeakerEventParam {

    private String mac;

    private String filename;

    private String text;

    private String sessionId;

    private Long id;
}
