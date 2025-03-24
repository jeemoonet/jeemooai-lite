package com.jeemoo.api.activemq.listener;

import cn.hutool.json.JSONUtil;
import com.jeemoo.common.utils.spring.SpringUtils;
import com.jeemoo.system.domain.bo.AiDocumentBo;
import com.jeemoo.system.service.IAiDocumentService;
import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQBytesMessage;

import javax.jms.Message;
import javax.jms.MessageListener;

public class DocumentListener implements MessageListener {

    private IAiDocumentService documentService = SpringUtils.getBean(IAiDocumentService.class);

    @SneakyThrows
    @Override
    public void onMessage(Message message) {
        System.out.println(message);
        ActiveMQBytesMessage mqBytesMessage = (ActiveMQBytesMessage)message;
        long length = mqBytesMessage.getBodyLength();
        byte[] bytes = new byte[(int) length];
        mqBytesMessage.readBytes(bytes, (int) length);
        String data = new String(bytes);

        AiDocumentBo documentBo = JSONUtil.toBean(data,AiDocumentBo.class);
        documentService.updateByBo(documentBo);
    }
}
