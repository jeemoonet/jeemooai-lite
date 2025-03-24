package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 对话文件对象 ai_record_file
 *
 * @author ai
 * @date 2024-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_record_file")
public class AiRecordFile extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "uuid")
    private String uuid;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * icon
     */
    private String icon;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 识别内容
     */
    private String content;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件url地址
     */
    private String fileUrl;
    /**
     * 
     */
    private Long recordId;

}
