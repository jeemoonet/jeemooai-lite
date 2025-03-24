package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 推荐问题业务对象 ai_recommend_question
 *
 * @author ai
 * @date 2024-05-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiRecommendQuestionBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long questionId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    private Integer sortNum;

    private String detail;
}
