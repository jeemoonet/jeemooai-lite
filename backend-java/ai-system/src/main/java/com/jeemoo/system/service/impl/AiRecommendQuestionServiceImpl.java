package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiRecommendQuestionBo;
import com.jeemoo.system.domain.vo.AiRecommendQuestionVo;
import com.jeemoo.system.domain.AiRecommendQuestion;
import com.jeemoo.system.mapper.AiRecommendQuestionMapper;
import com.jeemoo.system.service.IAiRecommendQuestionService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 推荐问题Service业务层处理
 *
 * @author ai
 * @date 2024-05-29
 */
@RequiredArgsConstructor
@Service
public class AiRecommendQuestionServiceImpl implements IAiRecommendQuestionService {

    private final AiRecommendQuestionMapper baseMapper;

    /**
     * 查询推荐问题
     */
    @Override
    public AiRecommendQuestionVo queryById(Long questionId){
        return baseMapper.selectVoById(questionId);
    }

    /**
     * 查询推荐问题列表
     */
    @Override
    public TableDataInfo<AiRecommendQuestionVo> queryPageList(AiRecommendQuestionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiRecommendQuestion> lqw = buildQueryWrapper(bo);
        Page<AiRecommendQuestionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询推荐问题列表
     */
    @Override
    public List<AiRecommendQuestionVo> queryList(AiRecommendQuestionBo bo) {
        LambdaQueryWrapper<AiRecommendQuestion> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiRecommendQuestion> buildQueryWrapper(AiRecommendQuestionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiRecommendQuestion> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), AiRecommendQuestion::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), AiRecommendQuestion::getContent, bo.getContent());
        return lqw;
    }

    /**
     * 新增推荐问题
     */
    @Override
    public Boolean insertByBo(AiRecommendQuestionBo bo) {
        AiRecommendQuestion add = BeanUtil.toBean(bo, AiRecommendQuestion.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setQuestionId(add.getQuestionId());
        }
        return flag;
    }

    /**
     * 修改推荐问题
     */
    @Override
    public Boolean updateByBo(AiRecommendQuestionBo bo) {
        AiRecommendQuestion update = BeanUtil.toBean(bo, AiRecommendQuestion.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiRecommendQuestion entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除推荐问题
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<AiRecommendQuestion> list(QueryWrapper<AiRecommendQuestion> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }
}
