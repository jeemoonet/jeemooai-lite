package com.jeemoo.system.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;
import java.util.List;


/**
 * 提示器视图对象 ai_prompt
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiPromptVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long promptId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String promptName;

    /**
     * 介绍
     */
    @ExcelProperty(value = "介绍")
    private String promptDesc;

    /**
     * 提示器类型：1=数字员工,2=应用场景,3=文案创作
     */
    @ExcelProperty(value = "提示器类型：1=数字员工,2=应用场景,3=文案创作")
    private Integer promptType;

    /**
     *
     */
    @ExcelProperty(value = "")
    private String promptIcon;

    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人")
    private Long userId;
    private String nickName;
    private String userName;

    /**
     * 初始化提示语(发送给gpt的)
     */
    @ExcelProperty(value = "初始化提示语(发送给gpt的)")
    private String initPrompt;

    /**
     * 初始化提示语(用户看到的)
     */
    @ExcelProperty(value = "初始化提示语(用户看到的)")
    private String initMessage;
    private String initMessageSrc;

    /**
     * 1=公开
     */
    @ExcelProperty(value = "1=公开")
    private Integer isPublic;

    private Integer isShare;

    private Integer isRecommend;

    private Integer isNew;

    private Integer isContext;

    private Integer status;

    /**
     * 使用人数
     */
    @ExcelProperty(value = "使用人数")
    private Long useNum;

    /**
     *
     */
    @ExcelProperty(value = "")
    private String model;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer maxTokens;

    /**
     *
     */
    @ExcelProperty(value = "")
    private BigDecimal temperature;

    /**
     * 倒序
     */
    @ExcelProperty(value = "倒序")
    private Long sortNumber;

    /**
     * 分类
     */
    @ExcelProperty(value = "分类")
    private Long categoryId;
    private String categoryName;

    private Date createTime;

    private Long companyId;
    private String companyName;

    @JsonIgnore
    private String docCategoryIds;

    @JsonProperty("docCategoryIds")
    private List<Long> docCategoryIdList;

    private List<AiPromptTipsVo> tips;

    private Integer topK;

    private String imgUrl;

    private String background;

    private String voice;

    private Integer isMinprogramShare;

    private Integer isRobotShare;

    private Integer isSend;

    private String customerUuid;

    private Integer isShowImg;

    private Integer isDoceShow;

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
