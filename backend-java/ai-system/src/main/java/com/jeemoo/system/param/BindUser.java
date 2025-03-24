package com.jeemoo.system.param;

import lombok.Data;

@Data
public class BindUser {

    private String companyName;

    private Long userId;

    private String userName;

    private String type;

    private Integer isLastLogin;
}
