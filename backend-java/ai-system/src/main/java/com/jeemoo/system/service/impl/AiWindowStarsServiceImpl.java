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
import com.jeemoo.system.domain.bo.AiWindowStarsBo;
import com.jeemoo.system.domain.vo.AiWindowStarsVo;
import com.jeemoo.system.domain.AiWindowStars;
import com.jeemoo.system.mapper.AiWindowStarsMapper;
import com.jeemoo.system.service.IAiWindowStarsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 评分Service业务层处理
 *
 * @author ai
 * @date 2024-01-11
 */
@RequiredArgsConstructor
@Service
public class AiWindowStarsServiceImpl implements IAiWindowStarsService {

    private final AiWindowStarsMapper baseMapper;

    /**
     * 查询评分
     */
    @Override
    public AiWindowStarsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询评分列表
     */
    @Override
    public TableDataInfo<AiWindowStarsVo> queryPageList(AiWindowStarsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiWindowStars> lqw = buildQueryWrapper(bo);
        Page<AiWindowStarsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询评分列表
     */
    @Override
    public List<AiWindowStarsVo> queryList(AiWindowStarsBo bo) {
        LambdaQueryWrapper<AiWindowStars> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiWindowStars> buildQueryWrapper(AiWindowStarsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiWindowStars> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getWindowId() != null, AiWindowStars::getWindowId, bo.getWindowId());
        lqw.eq(bo.getPromptId() != null, AiWindowStars::getPromptId, bo.getPromptId());
        lqw.eq(bo.getMemberId() != null, AiWindowStars::getMemberId, bo.getMemberId());
        lqw.eq(bo.getStar() != null, AiWindowStars::getStar, bo.getStar());
        return lqw;
    }

    /**
     * 新增评分
     */
    @Override
    public Boolean insertByBo(AiWindowStarsBo bo) {
        AiWindowStars add = BeanUtil.toBean(bo, AiWindowStars.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改评分
     */
    @Override
    public Boolean updateByBo(AiWindowStarsBo bo) {
        AiWindowStars update = BeanUtil.toBean(bo, AiWindowStars.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiWindowStars entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除评分
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
