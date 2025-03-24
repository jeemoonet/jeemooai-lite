package com.jeemoo.system.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PreviewResponse {

    private Integer orderType;
    private String service;
    private String currentPackage;
    private String startTime;
    private Date endTime;
    private BigDecimal price;
    private String priceInfo;
    private Integer packageDays;
    private Integer year;
    private Integer integral;
}
