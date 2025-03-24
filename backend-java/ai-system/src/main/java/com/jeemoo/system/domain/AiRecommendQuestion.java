package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 推荐问题对象 ai_recommend_question
 *
 * @author ai
 * @date 2024-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_recommend_question")
public class AiRecommendQuestion extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "question_id")
    private Long questionId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    private Integer sortNum;

    private String detail;
}
