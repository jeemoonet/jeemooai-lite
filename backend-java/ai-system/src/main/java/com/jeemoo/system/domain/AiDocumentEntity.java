package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 文档向量关系对象 ai_document_entity
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_document_entity")
public class AiDocumentEntity extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    private Long id;
    /**
     * 
     */
    private Long documentId;
    /**
     * 
     */
    private String entityId;

}
