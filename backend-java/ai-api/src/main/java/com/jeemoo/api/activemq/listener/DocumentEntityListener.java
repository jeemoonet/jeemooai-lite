package com.jeemoo.api.activemq.listener;

import cn.hutool.json.JSONUtil;
import com.jeemoo.common.utils.spring.SpringUtils;
import com.jeemoo.system.domain.bo.AiDocumentEntityBo;
import com.jeemoo.system.service.IAiDocumentEntityService;
import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQBytesMessage;

import javax.jms.Message;
import javax.jms.MessageListener;

public class DocumentEntityListener implements MessageListener {

    private IAiDocumentEntityService documentEntityService = SpringUtils.getBean(IAiDocumentEntityService.class);

    @SneakyThrows
    @Override
    public void onMessage(Message message) {
        System.out.println(message);
        ActiveMQBytesMessage mqBytesMessage = (ActiveMQBytesMessage)message;
        long length = mqBytesMessage.getBodyLength();
        byte[] bytes = new byte[(int) length];
        mqBytesMessage.readBytes(bytes, (int) length);
        String data = new String(bytes);

        AiDocumentEntityBo bo = JSONUtil.toBean(data, AiDocumentEntityBo.class);
        documentEntityService.insertByBo(bo);
    }
}
