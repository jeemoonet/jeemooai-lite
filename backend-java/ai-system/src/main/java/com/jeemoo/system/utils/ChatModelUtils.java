package com.jeemoo.system.utils;

import com.jeemoo.system.domain.AiModels;

import java.util.HashMap;
import java.util.List;

public class ChatModelUtils {

    public static String PLATFORM_OPENAI = "openai";
    public static String PLATFORM_MINIMAX = "minimax";
    public static String PLATFORM_ZHIPU = "zhipu";
    public static String PLATFORM_QIAN_FAN = "qian_fan";

    public static String PLATFORM_CLAUDE = "claude";
    public static String PLATFORM_CLAUDE_LLAMA = "claude_llama";


    public static String modelToPlatform(String modelName, List<AiModels> models) {
        for (AiModels model : models) {
            if (modelName.equals(model.getModelLabel())){
                return model.getPlatform();
            }
        }

        return PLATFORM_OPENAI;
    }
}
