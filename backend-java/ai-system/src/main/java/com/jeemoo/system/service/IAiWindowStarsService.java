package com.jeemoo.system.service;

import com.jeemoo.system.domain.AiWindowStars;
import com.jeemoo.system.domain.vo.AiWindowStarsVo;
import com.jeemoo.system.domain.bo.AiWindowStarsBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 评分Service接口
 *
 * @author ai
 * @date 2024-01-11
 */
public interface IAiWindowStarsService {

    /**
     * 查询评分
     */
    AiWindowStarsVo queryById(Long id);

    /**
     * 查询评分列表
     */
    TableDataInfo<AiWindowStarsVo> queryPageList(AiWindowStarsBo bo, PageQuery pageQuery);

    /**
     * 查询评分列表
     */
    List<AiWindowStarsVo> queryList(AiWindowStarsBo bo);

    /**
     * 新增评分
     */
    Boolean insertByBo(AiWindowStarsBo bo);

    /**
     * 修改评分
     */
    Boolean updateByBo(AiWindowStarsBo bo);

    /**
     * 校验并批量删除评分信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
