package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 提示器常用语视图对象 ai_prompt_tips
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiPromptTipsVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     *
     */
    @ExcelProperty(value = "")
    private String title;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long promptId;

    /**
     * 提示语
     */
    @ExcelProperty(value = "提示语")
    private String message;

    /**
     *
     */
    @JsonIgnore
    private Integer deleted;


}
