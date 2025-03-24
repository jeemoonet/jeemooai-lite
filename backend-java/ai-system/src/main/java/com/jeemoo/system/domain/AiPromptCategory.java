package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 提示器分类对象 ai_prompt_category
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_prompt_category")
public class AiPromptCategory extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "category_id")
    private Long categoryId;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     *
     */
    private String categoryIcon;
    /**
     *
     */
    @TableLogic
    private Integer deleted;

    private Long companyId;

    private Integer sortNum;
}
