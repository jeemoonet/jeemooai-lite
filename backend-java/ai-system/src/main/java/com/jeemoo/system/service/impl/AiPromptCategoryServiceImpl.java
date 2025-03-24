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
import com.jeemoo.system.domain.bo.AiPromptCategoryBo;
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.domain.AiPromptCategory;
import com.jeemoo.system.mapper.AiPromptCategoryMapper;
import com.jeemoo.system.service.IAiPromptCategoryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 提示器分类Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiPromptCategoryServiceImpl implements IAiPromptCategoryService {

    private final AiPromptCategoryMapper baseMapper;

    /**
     * 查询提示器分类
     */
    @Override
    public AiPromptCategoryVo queryById(Long categoryId){
        return baseMapper.selectVoById(categoryId);
    }

    /**
     * 查询提示器分类列表
     */
    @Override
    public TableDataInfo<AiPromptCategoryVo> queryPageList(AiPromptCategoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiPromptCategory> lqw = buildQueryWrapper(bo);
        Page<AiPromptCategoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询提示器分类列表
     */
    @Override
    public List<AiPromptCategoryVo> queryList(AiPromptCategoryBo bo) {
        LambdaQueryWrapper<AiPromptCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiPromptCategory> buildQueryWrapper(AiPromptCategoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiPromptCategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCategoryName()), AiPromptCategory::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryIcon()), AiPromptCategory::getCategoryIcon, bo.getCategoryIcon());
        lqw.eq(bo.getCompanyId() != null && bo.getCompanyId() != 0L, AiPromptCategory::getCompanyId, bo.getCompanyId());
        lqw.like(bo.getKeywords()!=null&&bo.getKeywords()!="",AiPromptCategory::getCategoryName,bo.getKeywords());
        lqw.orderByDesc(AiPromptCategory::getCreateTime);
        return lqw;
    }

    /**
     * 新增提示器分类
     */
    @Override
    public Boolean insertByBo(AiPromptCategoryBo bo) {
        AiPromptCategory add = BeanUtil.toBean(bo, AiPromptCategory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setCategoryId(add.getCategoryId());
        }
        return flag;
    }

    /**
     * 修改提示器分类
     */
    @Override
    public Boolean updateByBo(AiPromptCategoryBo bo) {
        AiPromptCategory update = BeanUtil.toBean(bo, AiPromptCategory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiPromptCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除提示器分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean deleteById(Long categoryId) {
        return baseMapper.deleteById(categoryId) > 0;
    }

    @Override
    public List<AiPromptCategory> list(QueryWrapper<AiPromptCategory> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void insert(AiPromptCategory aiPromptCategory) {
        baseMapper.insert(aiPromptCategory);
    }
}
