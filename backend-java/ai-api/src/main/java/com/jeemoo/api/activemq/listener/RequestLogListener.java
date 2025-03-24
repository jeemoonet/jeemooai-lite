package com.jeemoo.api.activemq.listener;

import cn.hutool.json.JSONUtil;
import com.jeemoo.common.utils.spring.SpringUtils;
import com.jeemoo.system.domain.AiRequestLog;
import com.jeemoo.system.service.IAiRequestLogService;
import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQBytesMessage;

import javax.jms.Message;
import javax.jms.MessageListener;

public class RequestLogListener implements MessageListener {

    private final IAiRequestLogService requestLogService = SpringUtils.getBean(IAiRequestLogService.class);

    @SneakyThrows
    @Override
    public void onMessage(Message message) {
        System.out.println(message);
        ActiveMQBytesMessage mqBytesMessage = (ActiveMQBytesMessage)message;
        long length = mqBytesMessage.getBodyLength();
        byte[] bytes = new byte[(int) length];
        mqBytesMessage.readBytes(bytes, (int) length);
        String data = new String(bytes);

        AiRequestLog requestLog = JSONUtil.toBean(data, AiRequestLog.class);
        requestLogService.insertOrUpdate(requestLog);

    }
}
