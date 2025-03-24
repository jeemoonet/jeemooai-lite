package com.jeemoo.api.activemq.listener;

import cn.hutool.json.JSONUtil;
import com.jeemoo.common.utils.spring.SpringUtils;
import com.jeemoo.system.domain.AiRecord;
import com.jeemoo.system.service.IAiRecordService;
import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQBytesMessage;

import javax.jms.Message;
import javax.jms.MessageListener;


public class RecordListener implements MessageListener {

    private final IAiRecordService recordService = SpringUtils.getBean(IAiRecordService.class);

    @SneakyThrows
    @Override
    public void onMessage(Message message) {
        System.out.println(message);
        ActiveMQBytesMessage mqBytesMessage = (ActiveMQBytesMessage)message;
        long length = mqBytesMessage.getBodyLength();
        byte[] bytes = new byte[(int) length];
        mqBytesMessage.readBytes(bytes, (int) length);
        String data = new String(bytes);

        AiRecord record = JSONUtil.toBean(data, AiRecord.class);
        record.setContent(record.getContent());
        if (record.getIsError() == 1) {
            record.setDocInfo(null);
        }
        recordService.updateById(record);
        recordService.updateErrorByMasterUuid(record.getMasterUuid(),record.getIsError());
    }
}
