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
import com.jeemoo.system.domain.bo.AiDocumentEntityBo;
import com.jeemoo.system.domain.vo.AiDocumentEntityVo;
import com.jeemoo.system.domain.AiDocumentEntity;
import com.jeemoo.system.mapper.AiDocumentEntityMapper;
import com.jeemoo.system.service.IAiDocumentEntityService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 文档向量关系Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiDocumentEntityServiceImpl implements IAiDocumentEntityService {

    private final AiDocumentEntityMapper baseMapper;

    /**
     * 查询文档向量关系
     */
    @Override
    public AiDocumentEntityVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询文档向量关系列表
     */
    @Override
    public TableDataInfo<AiDocumentEntityVo> queryPageList(AiDocumentEntityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiDocumentEntity> lqw = buildQueryWrapper(bo);
        Page<AiDocumentEntityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询文档向量关系列表
     */
    @Override
    public List<AiDocumentEntityVo> queryList(AiDocumentEntityBo bo) {
        LambdaQueryWrapper<AiDocumentEntity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiDocumentEntity> buildQueryWrapper(AiDocumentEntityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiDocumentEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDocumentId() != null, AiDocumentEntity::getDocumentId, bo.getDocumentId());
        lqw.eq(StringUtils.isNotBlank(bo.getEntityId()), AiDocumentEntity::getEntityId, bo.getEntityId());
        return lqw;
    }

    /**
     * 新增文档向量关系
     */
    @Override
    public Boolean insertByBo(AiDocumentEntityBo bo) {
        AiDocumentEntity add = BeanUtil.toBean(bo, AiDocumentEntity.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改文档向量关系
     */
    @Override
    public Boolean updateByBo(AiDocumentEntityBo bo) {
        AiDocumentEntity update = BeanUtil.toBean(bo, AiDocumentEntity.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiDocumentEntity entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除文档向量关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Long> getEntityIds(List<Long> documentIds) {
        return baseMapper.getEntityIds(documentIds);
    }

    @Override
    public boolean delete(QueryWrapper<AiDocumentEntity> wrapper) {
        return baseMapper.delete(wrapper) > 0;
    }
}
