package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;



/**
 * 评分视图对象 ai_window_stars
 *
 * @author ai
 * @date 2024-01-11
 */
@Data
@ExcelIgnoreUnannotated
public class AiWindowStarsVo {

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
    private Long windowId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long promptId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long memberId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private BigDecimal star;


}
