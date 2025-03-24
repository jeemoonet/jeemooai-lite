package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 聊天记录点赞不喜欢视图对象 ai_record_like
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiRecordLikeVo {

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
    private Long recordId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long userId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String content;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Integer isLike;


}
