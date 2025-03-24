package com.jeemoo.system.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiPromptTips;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class PromptCreateParam {

    private Long promptId;

    @NotEmpty(message = "名称不能为空")
    @Length(max = 100, message = "名称长度不能超过100个字符")
    private String promptName;

//    @NotEmpty(message = "简介不能为空")
    @Length(max = 200, message = "简介长度不能超过200个字符")
    private String promptDesc;

    @NotEmpty(message = "icon不能为空")
    private String promptIcon;

    private Integer promptType;

//    @NotNull(message = "助手分类不能为空", groups = { EditGroup.class })
    private Long categoryId;

//    @NotNull(message = "是否公开不能为空")
    private Integer isPublic;

//    @NotNull(message = "是否分享不能为空")
    private Integer isShare;

    private Integer isRecommend;

    private Integer isNew;

    private Integer isContext;

//    @NotEmpty(message = "用户欢迎语不能为空", groups = { EditGroup.class })
    @Length(max = 500, message = "用户欢迎语长度不能超过500个字符")
    private String initMessage = "你好，有什么能帮助的吗？";

    @Length(max = 10000, message = "角色设置长度不能超过10000个字符")
    private String initPrompt = "";

    private List<AiPromptTips> tips;

    private List<String> docCategoryIds;

    private double maxTokens = 1000;

    private double temperature = 0.9;

    private String model;

    @JsonIgnore
    private LoginUser loginUser;

    private String imgUrl;

    private String background;

    private Integer isMinprogramShare;

    private Integer isRobotShare;

    private String voice;

    private Integer topK;

    private Integer isShowImg;

    private Integer isDoceShow;

    private String shareImgUrl;

    private Integer isWindowStar;

    private Long digitPerson;

    private Integer isDigitPersonShare;

    private Integer isRecommendQuestions;

    private String searchType;

    private Float score;

    private String recommendQuestionPrompt;

    private String workflowId;

    private int historyCount = 6;

    private Integer longHistory;

    private Integer isRtcShare;
}
