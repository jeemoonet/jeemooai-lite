package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Date;



/**
 * 文档视图对象 ai_document
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiDocumentVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long documentId;

    /**
     * 文档类型：pdf,txt,word,url,wechat
     */
    @ExcelProperty(value = "文档类型：pdf,txt,word,url,wechat	")
    private String documentType;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String documentName;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String documentDesc;

    /**
     * 字节数
     */
    @ExcelProperty(value = "字节数")
    private Long fileSize;

    /**
     * 状态：0=未处理；1=进行中；2=已完成；3=失败
     */
    @ExcelProperty(value = "状态：0=未处理；1=进行中；2=已完成；3=失败")
    private Integer status;

    /**
     * 页数
     */
    @ExcelProperty(value = "页数")
    @JsonProperty("pageNum")
    private Integer pageNumber;

    /**
     * 备注，url抓取记录，错误记录
     */
    @ExcelProperty(value = "备注，url抓取记录，错误记录")
    private String remark;

    /**
     *
     */
    @JsonIgnore
    private Integer deleted;

    @JsonIgnore
    private Long companyId;

    private Long categoryId;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String url;

    private Long userId;

    private String splitName;

    private Integer length;

    private String splitChar;

    private Integer splitId;

    private String type;

    private Long folderId;
}
