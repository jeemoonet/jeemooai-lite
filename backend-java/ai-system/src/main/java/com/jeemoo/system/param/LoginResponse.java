package com.jeemoo.system.param;

import lombok.Data;

@Data
public class LoginResponse {

    private int isRegister = 0;

    private String token;

    private String avatar;

    private String nickName;

    private String companyName;

    private Long windowId;

    private int isComplete = 1;

    private String mobile;

    private String channelSn;

    private Long channelShareId;

    private String email;

    private String sex;

    private String phonenumber;

    private String smsCode;

    private String userShareSn;

    private Long digitPersonId;
}
