package com.jeemoo.system.service;

import com.jeemoo.system.domain.AiSplitRule;
import com.jeemoo.system.domain.vo.AiSplitRuleVo;
import com.jeemoo.system.domain.bo.AiSplitRuleBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 拆分策略Service接口
 *
 * @author ai
 * @date 2023-10-19
 */
public interface IAiSplitRuleService {

    /**
     * 查询拆分策略
     */
    AiSplitRuleVo queryById(Integer id);

    /**
     * 查询拆分策略列表
     */
    TableDataInfo<AiSplitRuleVo> queryPageList(AiSplitRuleBo bo, PageQuery pageQuery);

    /**
     * 查询拆分策略列表
     */
    List<AiSplitRuleVo> queryList(AiSplitRuleBo bo);

    /**
     * 新增拆分策略
     */
    Boolean insertByBo(AiSplitRuleBo bo);

    /**
     * 修改拆分策略
     */
    Boolean updateByBo(AiSplitRuleBo bo);

    /**
     * 校验并批量删除拆分策略信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);
}
