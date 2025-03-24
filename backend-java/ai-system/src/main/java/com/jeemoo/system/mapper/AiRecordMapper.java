package com.jeemoo.system.mapper;

import com.jeemoo.system.domain.AiRecord;
import com.jeemoo.system.domain.vo.AiRecordVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 聊天记录Mapper接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface AiRecordMapper extends BaseMapperPlus<AiRecordMapper, AiRecord, AiRecordVo> {

    @Update("update ai_record set is_like = #{type} where message_id = #{messageId} and user_id = #{userId}")
    int likeOrUnlike(@Param("messageId") Long messageId,@Param("type") int type,@Param("userId") Long userId);

    @Update("update ai_record set is_error = #{isError} where next_id = #{messageId}")
    void updateErrorByNextId(@Param("messageId") Long messageId, @Param("isError") Integer isError);

    @Update("update ai_record set deleted = 2 where master_uuid = #{masterUuid} and user_id = #{userId}")
    int deleteByMasterUuid(@Param("userId") Long userId,@Param("masterUuid") String masterUuid);

    @Update("update ai_record set is_context = 0 where window_id = #{windowId}")
    int clearContext(@Param("windowId") Long windowId);

    @Update("update ai_record set is_error = #{isError} where master_uuid = #{masterUuid}")
    void updateErrorByMasterUuid(@Param("masterUuid") String masterUuid,@Param("isError") Integer isError);
}
