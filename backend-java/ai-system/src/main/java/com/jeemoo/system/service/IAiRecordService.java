package com.jeemoo.system.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiRecord;
import com.jeemoo.system.domain.AiWindow;
import com.jeemoo.system.domain.vo.AiRecordVo;
import com.jeemoo.system.domain.bo.AiRecordBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.system.param.SendMessageParam;
import com.jeemoo.system.param.VectorData;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.Collection;
import java.util.List;

/**
 * 聊天记录Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiRecordService {

    /**
     * 查询聊天记录
     */
    AiRecordVo queryById(Long messageId);

    /**
     * 查询聊天记录列表
     */
    TableDataInfo<AiRecordVo> queryPageList(AiRecordBo bo, PageQuery pageQuery);

    /**
     * 查询聊天记录列表
     */
    List<AiRecordVo> queryList(AiRecordBo bo);

    /**
     * 新增聊天记录
     */
    Boolean insertByBo(AiRecordBo bo);

    /**
     * 修改聊天记录
     */
    Boolean updateByBo(AiRecordBo bo);

    /**
     * 校验并批量删除聊天记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<ChatMessage> fetchMessages(AiWindow window, LoginUser loginUser, Integer historyCount);
    List<ChatMessage> fetchMessages(AiWindow window, Long userId, Integer historyCount);
    AiRecord insertRecord(String message, AiWindow window, List<VectorData> vectorDataList, Long userId);
    AiRecord insertRecord(String message, AiWindow window, List<VectorData> vectorDataList, Long userId, Long memberId);
    AiRecord insertRecord(SendMessageParam param, AiWindow window, List<VectorData> vectorDataList, Long userId, Long memberId, List<JSONObject> searchList);

    void updateById(AiRecord record);

    boolean likeOrUnlike(Long messageId, int type, Long userId);

    void updateErrorByNextId(Long messageId, Integer isError);

    boolean deleteByMasterUuid(LoginUser loginUser, String masterUuid);

    boolean clearContext(AiWindow window);

    void updateErrorByMasterUuid(String masterUuid, Integer isError);

    List<AiRecord> list(QueryWrapper<AiRecord> queryWrapper);
}
