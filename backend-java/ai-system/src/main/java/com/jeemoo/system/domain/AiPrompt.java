package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 提示器对象 ai_prompt
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_prompt")
public class AiPrompt extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "prompt_id")
    private Long promptId;
    /**
     * 标题
     */
    private String promptName;
    /**
     * 介绍
     */
    private String promptDesc;
    /**
     * 提示器类型：1=数字员工,2=应用场景,3=文案创作
     */
    private Integer promptType;
    /**
     *
     */
    private String promptIcon;
    /**
     * 创建人
     */
    private Long userId;
    /**
     * 初始化提示语(发送给gpt的)
     */
    private String initPrompt;
    /**
     * 初始化提示语(用户看到的)
     */
    private String initMessage;
    /**
     * 1=公开
     */
    private Integer isPublic;

    private Integer isShare;
    /**
     * 使用人数
     */
    private Long useNum;
    /**
     *
     */
    private String model;
    /**
     *
     */
    private Integer maxTokens;
    /**
     *
     */
    private BigDecimal temperature;
    /**
     * 倒序
     */
    private Long sortNumber;
    /**
     * 分类
     */
    private Long categoryId;
    /**
     * 2=已删除
     */
    @TableLogic
    private Integer deleted;

    private Long companyId;

    private String docCategoryIds;

    private Integer isRecommend;

    private Integer isNew;

    private Integer isContext;

    private Integer status;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer topK;

    private String imgUrl;

    private String background;

    private String voice;

    private Integer isMinprogramShare;

    private Integer isRobotShare;

    private Integer isShowImg;

    private Integer isDoceShow;

    private Long repositoryId;

    private String shareImgUrl;

    private Integer isWindowStar;

    private String themeColor;

    private Long digitPerson;

    private Integer isDigitPersonShare;

    private Integer isRecommendQuestions;

    private String searchType;

    private Float score;

    private String recommendQuestionPrompt;

    private String workflowId;

    private Integer historyCount;

    private Integer longHistory;

    private Integer isRtcShare;
}
