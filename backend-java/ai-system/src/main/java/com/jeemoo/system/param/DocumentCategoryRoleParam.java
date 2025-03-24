package com.jeemoo.system.param;

import lombok.Data;

import java.util.List;

@Data
public class DocumentCategoryRoleParam {

    private Long categoryId;

    private List<Long> roleIds;
}
