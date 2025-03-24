package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 拆分策略业务对象 ai_split_rule
 *
 * @author ai
 * @date 2023-10-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiSplitRuleBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 拆分字符
     */
    @NotBlank(message = "拆分字符不能为空", groups = { AddGroup.class, EditGroup.class })
    private String splitChar;

    /**
     * 拆分长度
     */
    @NotNull(message = "拆分长度不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer length;

    /**
     * 策略名称
     */
    @NotBlank(message = "策略名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;


}
