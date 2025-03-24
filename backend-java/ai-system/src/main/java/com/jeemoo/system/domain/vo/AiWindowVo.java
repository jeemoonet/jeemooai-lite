package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;



/**
 * 窗口视图对象 ai_window
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiWindowVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long windowId;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long userId;

    /**
     * 窗口名称
     */
    @ExcelProperty(value = "窗口名称")
    private String windowName;

    /**
     * 0=普通，1=测试，2=外部客服
     */
    @ExcelProperty(value = "0=普通，1=测试，2=外部客服")
    private Integer talkType;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long promptId;

    private String customerUuid;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer deleted;

    private BigDecimal star;

    private Long companyId;

    private String commonUserId;
}
