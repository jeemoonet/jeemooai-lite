package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 模型视图对象 ai_models
 *
 * @author ai
 * @date 2023-11-15
 */
@Data
@ExcelIgnoreUnannotated
public class AiModelsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Integer id;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String modelName;

    private String platformName;
    /**
     * 
     */
    @ExcelProperty(value = "")
    private String modelLabel;

    private Integer isDefault;
    private Integer isReasoningDefault;

    private String icon;
}
