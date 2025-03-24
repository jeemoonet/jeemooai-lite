package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 文档对象 ai_document
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_document")
public class AiDocument extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "document_id")
    private Long documentId;
    /**
     * 文档类型：pdf,txt,word,url,wechat
     */
    private String documentType;
    /**
     * 名称
     */
    private String documentName;
    /**
     * 简介
     */
    private String documentDesc;
    /**
     * 字节数
     */
    private Long fileSize;
    /**
     * 状态：0=未处理；1=进行中；2=已完成；3=失败
     */
    private Integer status;
    /**
     * 页数
     */
    private Integer pageNumber;
    /**
     * 备注，url抓取记录，错误记录
     */
    private String remark;
    /**
     *
     */
    @TableLogic
    private Integer deleted;

    private Long companyId;

    private Long categoryId;

    private String url;

    private Long userId;

    private String splitName;

    private Integer length;

    private String splitChar;

    private Integer splitId;
}
