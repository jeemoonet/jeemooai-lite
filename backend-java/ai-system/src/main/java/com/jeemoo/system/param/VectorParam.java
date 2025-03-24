package com.jeemoo.system.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class VectorParam {

    private String id;

    @NotEmpty(message = "内容不能为空")
    private String content;

    @NotNull(message = "documentId不能为空")
    private Long documentId;

    private Long categoryId;
}
