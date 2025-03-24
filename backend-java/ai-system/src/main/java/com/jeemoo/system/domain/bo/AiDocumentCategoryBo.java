package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;
import java.util.List;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 知识库分类业务对象 ai_document_category
 *
 * @author ai
 * @date 2023-09-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiDocumentCategoryBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long categoryId;

    /**
     * 
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String categoryName;

    /**
     * 
     */
    private String categoryIcon;

    /**
     * 
     */
    private Long parentId;

    private Long companyId;

    private String path;

    private Long userId;

    private Integer isMy;

    private List<Long> roleIds;
}
