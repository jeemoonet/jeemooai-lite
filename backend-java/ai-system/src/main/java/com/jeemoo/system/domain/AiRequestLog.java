package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 请求记录对象 ai_request_log
 *
 * @author ai
 * @date 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_request_log")
public class AiRequestLog extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "uuid")
    @TableField(value = "`uuid`")
    private String uuid;
    /**
     * 
     */
    private Long userId;
    private Long memberId;
    /**
     * 
     */
    @TableField(value = "`key`")
    private String key;
    /**
     * 
     */
    private Integer wordCount;
    /**
     * 
     */
    private Integer requestTime;
    /**
     * 
     */
    private String model;
    /**
     * 
     */
    private String errorMessage;
    /**
     * 发送给gpt的数据
     */
    private String sendMessage;
    /**
     * gpt返回的数据
     */
    private String receiveMessage;
    /**
     * 
     */
    private Long promptId;

    private String ip;

    private String userAgent;

    private String question;

    private Long companyId;

    private Integer integral;

    private String functionName;
}
