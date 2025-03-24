package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 拆分策略视图对象 ai_split_rule
 *
 * @author ai
 * @date 2023-10-19
 */
@Data
@ExcelIgnoreUnannotated
public class AiSplitRuleVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Integer id;

    /**
     * 拆分字符
     */
    @ExcelProperty(value = "拆分字符")
    private String splitChar;

    /**
     * 拆分长度
     */
    @ExcelProperty(value = "拆分长度")
    private Integer length;

    /**
     * 策略名称
     */
    @ExcelProperty(value = "策略名称")
    private String name;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;


}
