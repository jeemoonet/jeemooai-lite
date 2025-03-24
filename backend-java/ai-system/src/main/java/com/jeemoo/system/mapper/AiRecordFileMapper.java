package com.jeemoo.system.mapper;

import com.jeemoo.system.domain.AiRecordFile;
import com.jeemoo.system.domain.vo.AiRecordFileVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对话文件Mapper接口
 *
 * @author ai
 * @date 2024-05-16
 */
public interface AiRecordFileMapper extends BaseMapperPlus<AiRecordFileMapper, AiRecordFile, AiRecordFileVo> {

    void updateRecordId(@Param("messageId") Long messageId,@Param("filesUuid") List<String> filesUuid);

    List<AiRecordFile> getFileByRecordIds(@Param("recordIds") List<Long> recordIds);
}
