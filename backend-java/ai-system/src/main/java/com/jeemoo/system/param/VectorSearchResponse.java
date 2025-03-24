package com.jeemoo.system.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VectorSearchResponse {

    private String documentName;

    private String content;

    private String id;

    private Float score;
}
