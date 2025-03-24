package com.jeemoo.system.param;

import lombok.Data;

import java.util.List;

@Data
public class ChatPromptSelectResponse {

    private String categoryName;

    private String categoryIcon;

    private Long categoryId;

    private List<PromptListResponse> children;
}
