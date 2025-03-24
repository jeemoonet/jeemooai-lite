package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 提示器分类视图对象 ai_prompt_category
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiPromptCategoryVo {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String categoryIcon;

    private Integer sortNum;

    private Date createTime;
    /**
     * 
     */
    @JsonIgnore
    private Integer deleted;

    @JsonIgnore
    private Long companyId;
}
