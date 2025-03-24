package com.jeemoo.system.param;

import lombok.Data;

@Data
public class AiModels {

    private String modelName;

    private String modelLabel;

    private Integer function;

    private String icon;

    private String platformName;

    public static AiModels build(String modelName, String modelLabel, Integer function, String icon) {
        AiModels aiModels = new AiModels();
        aiModels.setModelName(modelName);
        aiModels.setModelLabel(modelLabel);
        aiModels.setFunction(function);
        aiModels.setIcon(icon);
        return aiModels;
    }
}
