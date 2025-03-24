package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 模型业务对象 ai_models
 *
 * @author ai
 * @date 2023-11-15
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiModelsBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 
     */
    @NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modelName;

    /**
     * 
     */
    @NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modelLabel;

    private String platformName;

    private String icon;
}
