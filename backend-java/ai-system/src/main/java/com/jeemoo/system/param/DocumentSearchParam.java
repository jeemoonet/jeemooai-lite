package com.jeemoo.system.param;

import lombok.Data;

@Data
public class DocumentSearchParam {

    private Long categoryId;

    private Integer status;

    private String keywords;
}
