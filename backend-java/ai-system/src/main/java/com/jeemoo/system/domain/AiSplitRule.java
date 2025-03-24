package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 拆分策略对象 ai_split_rule
 *
 * @author ai
 * @date 2023-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_split_rule")
public class AiSplitRule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 拆分字符
     */
    private String splitChar;
    /**
     * 拆分长度
     */
    private Integer length;
    /**
     * 策略名称
     */
    @TableField(value = "`name`")
    private String name;

}
