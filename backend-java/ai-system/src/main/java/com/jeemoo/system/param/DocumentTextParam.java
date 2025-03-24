package com.jeemoo.system.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DocumentTextParam {

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @NotEmpty(message = "链接地址不能为空")
    private String text;
}
