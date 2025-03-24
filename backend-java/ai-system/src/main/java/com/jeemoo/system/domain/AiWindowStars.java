package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 评分对象 ai_window_stars
 *
 * @author ai
 * @date 2024-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_window_stars")
public class AiWindowStars extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 
     */
    private Long windowId;
    /**
     * 
     */
    private Long promptId;
    /**
     * 
     */
    private Long memberId;
    /**
     * 
     */
    private BigDecimal star;

    private Long companyId;
}
