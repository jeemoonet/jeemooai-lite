package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 提示器业务对象 ai_prompt
 *
 * @author ai
 * @date 2023-09-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiPromptBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long promptId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String promptName;

    /**
     * 介绍
     */
    @NotBlank(message = "介绍不能为空", groups = { AddGroup.class, EditGroup.class })
    private String promptDesc;

    /**
     * 提示器类型：1=数字员工,2=应用场景,3=文案创作
     */
    @NotNull(message = "提示器类型：1=数字员工,2=应用场景,3=文案创作不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer promptType;

    /**
     *
     */
    @NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String promptIcon;

    /**
     * 创建人
     */
    @NotNull(message = "创建人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;
    private String nickName;
    private String userName;

    /**
     * 初始化提示语(发送给gpt的)
     */
    @NotBlank(message = "初始化提示语(发送给gpt的)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String initPrompt;

    /**
     * 初始化提示语(用户看到的)
     */
    @NotBlank(message = "初始化提示语(用户看到的)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String initMessage;

    /**
     * 1=公开
     */
    @NotNull(message = "1=公开不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer isPublic;

    private Integer isShare;

    private Integer isRecommend;

    private Integer isNew;

    private Integer isContext;

    private Integer status;

    /**
     * 使用人数
     */
    @NotNull(message = "使用人数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long useNum;

    /**
     *
     */
    @NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String model;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer maxTokens;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal temperature;

    /**
     * 倒序
     */
    @NotNull(message = "倒序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sortNumber;

    /**
     * 分类
     */
    @NotNull(message = "分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long categoryId;
    private String categoryName;

    /**
     * 2=已删除
     */
    @NotNull(message = "2=已删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;

    private Long companyId;
    private String companyName;

    private String docCategoryIds;

    private Integer topK;

    private String imgUrl;

    private String background;

    private String voice;

    private Integer isMinprogramShare;

    private Integer isRobotShare;

    private Integer isShowImg;

    private String shareImgUrl;

    private Integer isWindowStar;

    private String themeColor;

    private String className;

    private Integer type;

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
