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
import com.jeemoo.system.domain.bo.AiPromptTipsBo;
import com.jeemoo.system.domain.vo.AiPromptTipsVo;
import com.jeemoo.system.domain.AiPromptTips;
import com.jeemoo.system.mapper.AiPromptTipsMapper;
import com.jeemoo.system.service.IAiPromptTipsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 提示器常用语Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiPromptTipsServiceImpl implements IAiPromptTipsService {

    private final AiPromptTipsMapper baseMapper;

    /**
     * 查询提示器常用语
     */
    @Override
    public AiPromptTipsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询提示器常用语列表
     */
    @Override
    public TableDataInfo<AiPromptTipsVo> queryPageList(AiPromptTipsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiPromptTips> lqw = buildQueryWrapper(bo);
        Page<AiPromptTipsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询提示器常用语列表
     */
    @Override
    public List<AiPromptTipsVo> queryList(AiPromptTipsBo bo) {
        LambdaQueryWrapper<AiPromptTips> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiPromptTips> buildQueryWrapper(AiPromptTipsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiPromptTips> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), AiPromptTips::getTitle, bo.getTitle());
        lqw.eq(bo.getPromptId() != null, AiPromptTips::getPromptId, bo.getPromptId());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), AiPromptTips::getMessage, bo.getMessage());
        lqw.eq(bo.getDeleted() != null, AiPromptTips::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增提示器常用语
     */
    @Override
    public Boolean insertByBo(AiPromptTipsBo bo) {
        AiPromptTips add = BeanUtil.toBean(bo, AiPromptTips.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改提示器常用语
     */
    @Override
    public Boolean updateByBo(AiPromptTipsBo bo) {
        AiPromptTips update = BeanUtil.toBean(bo, AiPromptTips.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiPromptTips entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除提示器常用语
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean saveBatch(List<AiPromptTips> tipsSaveBatch) {
        return baseMapper.insertBatch(tipsSaveBatch);
    }
}
