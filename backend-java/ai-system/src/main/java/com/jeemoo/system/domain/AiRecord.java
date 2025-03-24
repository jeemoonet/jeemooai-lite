package com.jeemoo.system.domain;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.jeemoo.common.utils.PromptTransfer;
import com.jeemoo.system.param.VectorData;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.jeemoo.common.core.domain.BaseEntity;
import org.jetbrains.annotations.NotNull;

/**
 * 聊天记录对象 ai_record
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_record")
public class AiRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "message_id")
    private Long messageId;
    /**
     *
     */
    private Long windowId;
    /**
     *
     */
    private Long userId;

    private String type;
    /**
     * 内容
     */
    private String content;
    private String reasoningContent;
    /**
     * user=用户发送,assistant=机器人回复
     */
    private String role;
    /**
     *
     */
    private Integer isInitMessage;
    /**
     *
     */
    private Integer isLike;
    /**
     *
     */
    private Long createTimestamp;
    /**
     *
     */
    private String masterUuid;
    /**
     *
     */
    private String docInfo;

    @TableField(exist = false)
    private List<JSONObject> docInfoList;

    private String searchInfo;

    @TableField(exist = false)
    private List<JSONObject> searchInfoList;

    private Long promptId;

    private Long memberId;

    private Long nextId;

    private Long companyId;

    private String avatar;

    private Integer isError;
    /**
     *
     */
    @TableLogic
    private Integer deleted;


    public static ChatMessage getChatMessage(AiPrompt prompt, List<VectorData> vectorDataList, List<VectorData> longHistoryDataList) {
        ChatMessage message = new ChatMessage();
        message.setRole(ChatMessageRole.SYSTEM.value());
        StringBuilder promptContent = new StringBuilder();
        if (vectorDataList != null && !vectorDataList.isEmpty()) {
            promptContent.append("\n");
            for (VectorData vectorData : vectorDataList) {
                promptContent.append(vectorData.getContent());
                promptContent.append("\n");
            }
        }
        if (longHistoryDataList != null && !longHistoryDataList.isEmpty()) {
            promptContent.append("\n长期记忆:\n");
            for (VectorData vectorData : longHistoryDataList) {
                promptContent.append(vectorData.getContent());
                promptContent.append("\n");
            }
        }
        message.setContent(PromptTransfer.transfer(prompt.getInitPrompt(), "${knowledge}", promptContent.toString(), true));
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss EEEE");

        // 格式化当前时间
        String formattedDateTime = now.format(formatter);
        message.setContent(PromptTransfer.transfer(message.getContent(), "${time}", formattedDateTime, false));
        return message;
    }
}
