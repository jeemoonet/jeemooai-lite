package com.jeemoo.framework.config;

import com.jeemoo.framework.config.properties.EncryptorProperties;
import com.jeemoo.framework.manager.EncryptorManager;
import com.jeemoo.framework.encrypt.MybatisDecryptInterceptor;
import com.jeemoo.framework.encrypt.MybatisEncryptInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加解密配置
 *
 * @author 老马
 * @version 4.6.0
 */
@Configuration
@ConditionalOnProperty(value = "mybatis-encryptor.enable", havingValue = "true")
public class EncryptorConfig {

    @Autowired
    private EncryptorProperties properties;

    @Bean
    public EncryptorManager encryptorManager() {
        return new EncryptorManager();
    }

    @Bean
    public MybatisEncryptInterceptor mybatisEncryptInterceptor(EncryptorManager encryptorManager) {
        return new MybatisEncryptInterceptor(encryptorManager, properties);
    }

    @Bean
    public MybatisDecryptInterceptor mybatisDecryptInterceptor(EncryptorManager encryptorManager) {
        return new MybatisDecryptInterceptor(encryptorManager, properties);
    }
}
