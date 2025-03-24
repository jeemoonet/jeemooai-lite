package com.jeemoo.system.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CompanyWindowResponse {

    private Long windowId;

    private Integer talkType;

    private String nickName;

    private String avatar;

    private String mobile;

    private String userNickname;

    private String userAvatar;

    private Long userId;

    private Long promptId;

    private String promptName;

    private Float star;

    private Date createTime;
}
