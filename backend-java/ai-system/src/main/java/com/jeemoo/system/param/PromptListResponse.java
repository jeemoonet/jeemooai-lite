package com.jeemoo.system.param;

import lombok.Data;

import java.util.Date;

@Data
public class PromptListResponse {

    private Long promptId;

    private String promptName;

    private String promptIcon;

    private String promptDesc;

    private Integer useNum;

    private Long categoryId;

    private Integer promptType;

    private Integer isRecommend;

    private Integer isNew;

    private Integer isMy;

    private String nickName;

    private String avatar;

    private Date updateTime;

    private Integer publishState;
}
