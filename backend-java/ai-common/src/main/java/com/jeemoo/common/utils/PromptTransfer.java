package com.jeemoo.common.utils;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public class PromptTransfer {

    public static String transfer(String prompt, String var, String val, boolean isAppend) {
        if (prompt.contains(var)) {
            prompt = prompt.replace(var, val);
        } else if (isAppend) {
            prompt = prompt + "\n" + val;
        }

        return prompt;
    }

    public static String transfer(String prompt, String var, List<ChatMessage> messages, boolean isAppend) {
        StringBuilder val = new StringBuilder();
        for (ChatMessage message : messages) {
            val.append(message.getRole()).append(":").append(message.getContent()).append("\n");
//            if (message.getRole().equals("user")) {
//                val.append("问题：").append(message.getContent()).append("\n");
//            }
//            if (message.getRole().equals("assistant")) {
//                val.append("回答：").append(message.getContent()).append("\n");
//            }
        }

        return transfer(prompt, var, val.toString(), isAppend);
    }

}
