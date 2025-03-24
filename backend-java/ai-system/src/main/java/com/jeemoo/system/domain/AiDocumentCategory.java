package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 知识库分类对象 ai_document_category
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_document_category")
public class AiDocumentCategory extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "category_id")
    private Long categoryId;
    /**
     *
     */
    private String categoryName;
    /**
     *
     */
    private String categoryIcon;
    /**
     *
     */
    private Long parentId;
    /**
     *
     */
    @TableLogic
    private Integer deleted;

    private Long companyId;

    @TableField("`path`")
    private String path;

    private Long userId;

    private Integer isMy;
}
