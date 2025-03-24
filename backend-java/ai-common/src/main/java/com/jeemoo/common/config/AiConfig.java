package com.jeemoo.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author Lion Li
 */

@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiConfig {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private boolean demoEnabled;

    /**
     * 缓存懒加载
     */
    private boolean cacheLazy;

    private String pcUrl;

    private String apiUrl;

    private String previewUrl;

    /** 上传路径 */
    private static String profile;

    public void setProfile(String profile) {
        AiConfig.profile = profile;
    }

    public static String getProfile()
    {
        return profile;
    }
    /**
    /**
     * 获取地址开关
     */
    @Getter
    private static boolean addressEnabled;

    public void setAddressEnabled(boolean addressEnabled) {
        AiConfig.addressEnabled = addressEnabled;
    }

}
