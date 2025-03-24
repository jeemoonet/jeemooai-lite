package com.jeemoo.system.param;

import lombok.Data;

@Data
public class DocumentFolderSearchParam {

    private Long folderId;

    private Integer status;

    private String keywords;
}
