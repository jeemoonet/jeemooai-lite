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
import com.jeemoo.system.domain.bo.AiRecordLikeBo;
import com.jeemoo.system.domain.vo.AiRecordLikeVo;
import com.jeemoo.system.domain.AiRecordLike;
import com.jeemoo.system.mapper.AiRecordLikeMapper;
import com.jeemoo.system.service.IAiRecordLikeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 聊天记录点赞不喜欢Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiRecordLikeServiceImpl implements IAiRecordLikeService {

    private final AiRecordLikeMapper baseMapper;

    /**
     * 查询聊天记录点赞不喜欢
     */
    @Override
    public AiRecordLikeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询聊天记录点赞不喜欢列表
     */
    @Override
    public TableDataInfo<AiRecordLikeVo> queryPageList(AiRecordLikeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiRecordLike> lqw = buildQueryWrapper(bo);
        Page<AiRecordLikeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询聊天记录点赞不喜欢列表
     */
    @Override
    public List<AiRecordLikeVo> queryList(AiRecordLikeBo bo) {
        LambdaQueryWrapper<AiRecordLike> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiRecordLike> buildQueryWrapper(AiRecordLikeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiRecordLike> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, AiRecordLike::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), AiRecordLike::getContent, bo.getContent());
        lqw.eq(bo.getIsLike() != null, AiRecordLike::getIsLike, bo.getIsLike());
        return lqw;
    }

    /**
     * 新增聊天记录点赞不喜欢
     */
    @Override
    public Boolean insertByBo(AiRecordLikeBo bo) {
        AiRecordLike add = BeanUtil.toBean(bo, AiRecordLike.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改聊天记录点赞不喜欢
     */
    @Override
    public Boolean updateByBo(AiRecordLikeBo bo) {
        AiRecordLike update = BeanUtil.toBean(bo, AiRecordLike.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiRecordLike entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除聊天记录点赞不喜欢
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
