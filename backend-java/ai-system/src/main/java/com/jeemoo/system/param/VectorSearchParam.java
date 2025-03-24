package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VectorSearchParam {

    private Integer topK;

    @NotEmpty(message = "搜索内容不能为空")
    private String search;

    private List<String> categoryIds;

    private Float score;

    private String searchType;
}
