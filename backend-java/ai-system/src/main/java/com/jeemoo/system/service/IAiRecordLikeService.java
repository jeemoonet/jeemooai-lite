package com.jeemoo.system.service;

import com.jeemoo.system.domain.AiRecordLike;
import com.jeemoo.system.domain.vo.AiRecordLikeVo;
import com.jeemoo.system.domain.bo.AiRecordLikeBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 聊天记录点赞不喜欢Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiRecordLikeService {

    /**
     * 查询聊天记录点赞不喜欢
     */
    AiRecordLikeVo queryById(Long id);

    /**
     * 查询聊天记录点赞不喜欢列表
     */
    TableDataInfo<AiRecordLikeVo> queryPageList(AiRecordLikeBo bo, PageQuery pageQuery);

    /**
     * 查询聊天记录点赞不喜欢列表
     */
    List<AiRecordLikeVo> queryList(AiRecordLikeBo bo);

    /**
     * 新增聊天记录点赞不喜欢
     */
    Boolean insertByBo(AiRecordLikeBo bo);

    /**
     * 修改聊天记录点赞不喜欢
     */
    Boolean updateByBo(AiRecordLikeBo bo);

    /**
     * 校验并批量删除聊天记录点赞不喜欢信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
