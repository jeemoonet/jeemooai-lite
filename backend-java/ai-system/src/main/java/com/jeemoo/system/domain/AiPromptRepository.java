package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 公共库对象 ai_prompt_repository
 *
 * @author ai
 * @date 2023-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_prompt_repository")
public class AiPromptRepository extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 
     */
    private String promptName;
    /**
     * 
     */
    private String promptDesc;
    /**
     * 
     */
    private String initPrompt;
    /**
     * 
     */
    private String initMessage;
    /**
     * 
     */
    private Integer maxToken;
    /**
     * 
     */
    private BigDecimal temperature;
    /**
     * 
     */
    private String model;
    /**
     * 
     */
    private String promptIcon;
    /**
     * 
     */
    private Long categoryId;

    private String tipJson;

    private Long promptId;

    private Integer isPublic;

    private Integer isRecommend;

    private Integer status;

    private String functionJson;

    private Integer isDoceShow;
}
