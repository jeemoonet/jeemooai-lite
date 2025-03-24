package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 提示器文档对象 ai_prompt_document
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_prompt_document")
public class AiPromptDocument extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 
     */
    private Long promptId;
    /**
     * 
     */
    private Long documentId;
    /**
     * 
     */
    private Long categoryId;

}
