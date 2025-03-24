package com.jeemoo.api.activemq;

import com.jeemoo.api.activemq.listener.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;


/**
 * 配置类
 */
@Configuration
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.queueName.QUEUE_AI_DOCUMENT}")
    private String QUEUE_AI_DOCUMENT;

    @Value("${spring.queueName.QUEUE_AI_DOCUMENT_ENTITY}")
    private String QUEUE_AI_DOCUMENT_ENTITY;

    @Value("${spring.queueName.QUEUE_AI_RECORD}")
    private String QUEUE_AI_RECORD;

    @Value("${spring.queueName.QUEUE_AI_REQUEST_LOG}")
    private String QUEUE_AI_REQUEST_LOG;

    @Bean
    public ConnectionFactory connectionFactory(){
        return new ActiveMQConnectionFactory(user, password, brokerUrl);
    }

    // 在Queue模式中，对消息的监听需要对containerFactory进行配置
    @Bean("queueListener")
    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean
    public DefaultMessageListenerContainer AiRequestLogListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setDestinationName(QUEUE_AI_REQUEST_LOG);
        container.setMessageListener(new RequestLogListener());
        return container;
    }

    @Bean
    public DefaultMessageListenerContainer AiRecordListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setDestinationName(QUEUE_AI_RECORD);
        container.setMessageListener(new RecordListener());
        return container;
    }

    @Bean
    public DefaultMessageListenerContainer AiDocumentListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setDestinationName(QUEUE_AI_DOCUMENT);
        container.setMessageListener(new DocumentListener());
        return container;
    }

    @Bean
    public DefaultMessageListenerContainer AiDocumentEntityListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setDestinationName(QUEUE_AI_DOCUMENT_ENTITY);
        container.setMessageListener(new DocumentEntityListener());
        return container;
    }
}
