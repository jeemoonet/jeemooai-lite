package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 窗口业务对象 ai_window
 *
 * @author ai
 * @date 2023-09-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiWindowBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long windowId;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 窗口名称
     */
    @NotBlank(message = "窗口名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String windowName;

    /**
     * 0=普通，1=测试，2=外部客服
     */
    @NotNull(message = "0=普通，1=测试，2=外部客服不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer talkType;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long promptId;

    private String customerUuid;

    private Long companyId;

    private Long memberId;

    private BigDecimal star;

    private String platform;

    private String commonUserId;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;


}
