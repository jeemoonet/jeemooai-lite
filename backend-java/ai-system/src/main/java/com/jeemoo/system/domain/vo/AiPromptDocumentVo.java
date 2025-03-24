package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 提示器文档视图对象 ai_prompt_document
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiPromptDocumentVo {

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
    private Long promptId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long documentId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long categoryId;


}
