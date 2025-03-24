package com.jeemoo.system.param;

import lombok.Data;

@Data
public class ScanLoginData {

    private String uuid;

    private Long userId;

    private int status = 0;
}
