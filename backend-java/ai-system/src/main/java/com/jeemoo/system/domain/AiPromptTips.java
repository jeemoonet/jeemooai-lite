package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 提示器常用语对象 ai_prompt_tips
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_prompt_tips")
public class AiPromptTips extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     *
     */
    private String title;
    /**
     *
     */
    private Long promptId;
    /**
     * 提示语
     */
    private String message;
    /**
     *
     */
    @TableLogic
    private Integer deleted;

}
