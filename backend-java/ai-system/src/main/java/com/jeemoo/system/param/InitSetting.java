package com.jeemoo.system.param;

import lombok.Data;

@Data
public class InitSetting {

    private ModelInitConfig modelConfig;

    private EmbeddingInitConfig embeddingConfig;

    private BingInitConfig bingConfig;
}

