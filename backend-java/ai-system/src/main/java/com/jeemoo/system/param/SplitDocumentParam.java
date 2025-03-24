package com.jeemoo.system.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class SplitDocumentParam {

    private Integer id;

    @NotNull(message = "documentId不能为空")
    private Long documentId;

    private String splitChar;

    private Integer length;
}
