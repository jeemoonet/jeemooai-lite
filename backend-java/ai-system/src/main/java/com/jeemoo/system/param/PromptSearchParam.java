package com.jeemoo.system.param;

import lombok.Data;

@Data
public class PromptSearchParam {

    private Long categoryId;

    private String keywords;

    private Long companyId;

    private String promptName;

    private Integer isPublic;

    private Integer isShare;

    private Integer isRecommend;

    private Integer status;

    private String createBy;

    private Long userId;

    private Integer isManager;
}
