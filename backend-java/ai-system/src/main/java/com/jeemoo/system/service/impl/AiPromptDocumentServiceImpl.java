package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.domain.AiPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiPromptDocumentBo;
import com.jeemoo.system.domain.vo.AiPromptDocumentVo;
import com.jeemoo.system.domain.AiPromptDocument;
import com.jeemoo.system.mapper.AiPromptDocumentMapper;
import com.jeemoo.system.service.IAiPromptDocumentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 提示器文档Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiPromptDocumentServiceImpl implements IAiPromptDocumentService {

    private final AiPromptDocumentMapper baseMapper;

    /**
     * 查询提示器文档
     */
    @Override
    public AiPromptDocumentVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询提示器文档列表
     */
    @Override
    public TableDataInfo<AiPromptDocumentVo> queryPageList(AiPromptDocumentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiPromptDocument> lqw = buildQueryWrapper(bo);
        Page<AiPromptDocumentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询提示器文档列表
     */
    @Override
    public List<AiPromptDocumentVo> queryList(AiPromptDocumentBo bo) {
        LambdaQueryWrapper<AiPromptDocument> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiPromptDocument> buildQueryWrapper(AiPromptDocumentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiPromptDocument> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPromptId() != null, AiPromptDocument::getPromptId, bo.getPromptId());
        lqw.eq(bo.getDocumentId() != null, AiPromptDocument::getDocumentId, bo.getDocumentId());
        lqw.eq(bo.getCategoryId() != null, AiPromptDocument::getCategoryId, bo.getCategoryId());
        return lqw;
    }

    /**
     * 新增提示器文档
     */
    @Override
    public Boolean insertByBo(AiPromptDocumentBo bo) {
        AiPromptDocument add = BeanUtil.toBean(bo, AiPromptDocument.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改提示器文档
     */
    @Override
    public Boolean updateByBo(AiPromptDocumentBo bo) {
        AiPromptDocument update = BeanUtil.toBean(bo, AiPromptDocument.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiPromptDocument entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除提示器文档
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Long> fetchDocumentIds(AiPrompt prompt, LoginUser loginUser) {
        List<AiPromptDocument> list = baseMapper.selectList(new QueryWrapper<AiPromptDocument>().eq("prompt_id", prompt.getPromptId()));
        List<Long> ids = new ArrayList<>();
        for (AiPromptDocument aiPromptDocument : list) {
            ids.add(aiPromptDocument.getDocumentId());
        }
        return ids;
    }
}
