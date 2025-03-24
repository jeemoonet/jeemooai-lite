package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 聊天记录点赞不喜欢对象 ai_record_like
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_record_like")
public class AiRecordLike extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 
     */
    private Long messageId;
    /**
     * 
     */
    private Long userId;
    /**
     * 
     */
    private String content;
    /**
     * 
     */
    private Integer isLike;

}
