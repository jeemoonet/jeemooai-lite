package com.jeemoo.system.param;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class ScanLoginResponse {

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

    private Long userId;

    private List<BindUser> userList;
}
