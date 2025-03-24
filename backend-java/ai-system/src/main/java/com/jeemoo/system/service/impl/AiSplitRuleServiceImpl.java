package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiSplitRuleBo;
import com.jeemoo.system.domain.vo.AiSplitRuleVo;
import com.jeemoo.system.domain.AiSplitRule;
import com.jeemoo.system.mapper.AiSplitRuleMapper;
import com.jeemoo.system.service.IAiSplitRuleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 拆分策略Service业务层处理
 *
 * @author ai
 * @date 2023-10-19
 */
@RequiredArgsConstructor
@Service
public class AiSplitRuleServiceImpl implements IAiSplitRuleService {

    private final AiSplitRuleMapper baseMapper;

    /**
     * 查询拆分策略
     */
    @Override
    public AiSplitRuleVo queryById(Integer id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询拆分策略列表
     */
    @Override
    public TableDataInfo<AiSplitRuleVo> queryPageList(AiSplitRuleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiSplitRule> lqw = buildQueryWrapper(bo);
        Page<AiSplitRuleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询拆分策略列表
     */
    @Override
    public List<AiSplitRuleVo> queryList(AiSplitRuleBo bo) {
        LambdaQueryWrapper<AiSplitRule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiSplitRule> buildQueryWrapper(AiSplitRuleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiSplitRule> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSplitChar()), AiSplitRule::getSplitChar, bo.getSplitChar());
        lqw.eq(bo.getLength() != null, AiSplitRule::getLength, bo.getLength());
        lqw.like(StringUtils.isNotBlank(bo.getName()), AiSplitRule::getName, bo.getName());
        return lqw;
    }

    /**
     * 新增拆分策略
     */
    @Override
    public Boolean insertByBo(AiSplitRuleBo bo) {
        AiSplitRule add = BeanUtil.toBean(bo, AiSplitRule.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改拆分策略
     */
    @Override
    public Boolean updateByBo(AiSplitRuleBo bo) {
        AiSplitRule update = BeanUtil.toBean(bo, AiSplitRule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiSplitRule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除拆分策略
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
