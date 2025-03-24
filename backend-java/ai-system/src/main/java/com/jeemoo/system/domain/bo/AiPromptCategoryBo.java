package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 提示器分类业务对象 ai_prompt_category
 *
 * @author ai
 * @date 2023-09-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiPromptCategoryBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long categoryId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String categoryName;

    private String categoryIcon;

    private Long companyId;

    private String keywords;

    private Integer sortNum;
}
