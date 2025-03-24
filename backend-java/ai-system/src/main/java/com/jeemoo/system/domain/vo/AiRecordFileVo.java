package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 对话文件视图对象 ai_record_file
 *
 * @author ai
 * @date 2024-05-16
 */
@Data
@ExcelIgnoreUnannotated
public class AiRecordFileVo {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String uuid;

    /**
     * 文件名
     */
    @ExcelProperty(value = "文件名")
    private String fileName;

    /**
     * icon
     */
    @ExcelProperty(value = "icon")
    private String icon;

    /**
     * 文件大小
     */
    @ExcelProperty(value = "文件大小")
    private Long fileSize;

    /**
     * 识别内容
     */
    @ExcelProperty(value = "识别内容")
    private String content;

    /**
     * 文件类型
     */
    @ExcelProperty(value = "文件类型")
    private String fileType;

    /**
     * 文件url地址
     */
    @ExcelProperty(value = "文件url地址")
    private String fileUrl;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long recordId;


}
