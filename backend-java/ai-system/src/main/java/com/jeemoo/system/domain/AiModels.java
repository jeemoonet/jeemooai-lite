package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 模型对象 ai_models
 *
 * @author ai
 * @date 2023-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_models")
public class AiModels extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 
     */
    private String modelName;

    private String platformName;
    /**
     * 
     */
    private String modelLabel;


    private Integer isDefault;
    private Integer isReasoningDefault;

    private String platform;

    private String icon;
}
