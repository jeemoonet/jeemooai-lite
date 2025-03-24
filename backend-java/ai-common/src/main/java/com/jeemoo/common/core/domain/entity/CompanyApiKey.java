package com.jeemoo.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CompanyApiKey {

    private Long id;
    /**
     *
     */
    private Long companyId;
    /**
     *
     */
    private String apiKey;
    /**
     *
     */
    private Integer status;

    private Integer integral;

    private Integer advancedPackageLevel;
}
