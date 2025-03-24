package com.jeemoo.common.config;

import lombok.Data;

@Data
public class AccessToken {

    private String Id;

    private String UserId;

    private Long ExpireTime;
}
