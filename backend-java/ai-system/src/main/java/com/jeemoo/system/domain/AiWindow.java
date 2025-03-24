package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 窗口对象 ai_window
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_window")
public class AiWindow extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "window_id")
    private Long windowId;
    /**
     *
     */
    private Long userId;
    /**
     * 窗口名称
     */
    private String windowName;
    /**
     * 0=普通，1=测试，2=外部客服
     */
    private Integer talkType;
    /**
     *
     */
    private Long promptId;

    private String customerUuid;

    private Long companyId;

    private Long memberId;

    private BigDecimal star;

    private String uuid;

    private String model;

    private String platform;

    private String commonUserId;
    /**
     *
     */
    @TableLogic
    private Integer deleted;

}
