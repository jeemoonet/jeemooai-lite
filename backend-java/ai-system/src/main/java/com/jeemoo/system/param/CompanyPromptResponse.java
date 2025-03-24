package com.jeemoo.system.param;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyPromptResponse {

    private Long promptId;
    private Integer isPublic;
    private Integer isRecommend;
    private String initPrompt;
    private String promptName;
    private String promptIcon;
    private String createBy;
    private Integer status;
    private Integer isShare;
    private Date createTime;
}
