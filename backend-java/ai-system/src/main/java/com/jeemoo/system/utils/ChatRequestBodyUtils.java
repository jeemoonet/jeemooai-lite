package com.jeemoo.system.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.domain.AiPrompt;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChatRequestBodyUtils {

    public static void parse(JSONObject requestBody, AiPrompt outPrompt, List<ChatMessage> messages, List<AiModels> models) {
        AiPrompt prompt = BeanUtil.toBean(outPrompt, AiPrompt.class);
        openai(requestBody,prompt,messages);
    }


    private static void openai(JSONObject requestBody, AiPrompt prompt, List<ChatMessage> messages) {
        if (StrUtil.isNotEmpty(prompt.getModel())) {
            requestBody.set("model",prompt.getModel());
        }
        if (prompt.getMaxTokens() != null && prompt.getMaxTokens() > 0) {
            requestBody.set("max_tokens", prompt.getMaxTokens());
        }
        if (prompt.getTemperature() != null && prompt.getTemperature().compareTo(BigDecimal.ONE) <= 0) {
            requestBody.set("temperature", prompt.getTemperature());
        } else if (prompt.getTemperature() != null && prompt.getTemperature().compareTo(BigDecimal.ONE) > 0) {
            requestBody.set("temperature", 1);
        }

        requestBody.set("messages",messages);
    }
}
