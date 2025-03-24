package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.mapper.AiCompanyMapper;
import com.jeemoo.system.mapper.AiModelsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiRequestLogBo;
import com.jeemoo.system.domain.vo.AiRequestLogVo;
import com.jeemoo.system.domain.AiRequestLog;
import com.jeemoo.system.mapper.AiRequestLogMapper;
import com.jeemoo.system.service.IAiRequestLogService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 请求记录Service业务层处理
 *
 * @author ai
 * @date 2023-09-12
 */
@RequiredArgsConstructor
@Service
public class AiRequestLogServiceImpl implements IAiRequestLogService {

    private final AiRequestLogMapper baseMapper;
    private final AiModelsMapper modelsMapper;
    private final AiCompanyMapper companyMapper;

    /**
     * 查询请求记录
     */
    @Override
    public AiRequestLogVo queryById(String uuid){
        return baseMapper.selectVoById(uuid);
    }

    /**
     * 查询请求记录列表
     */
    @Override
    public TableDataInfo<AiRequestLogVo> queryPageList(AiRequestLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiRequestLog> lqw = buildQueryWrapper(bo);
        Page<AiRequestLogVo> result = baseMapper.listPage(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询请求记录列表
     */
    @Override
    public List<AiRequestLogVo> queryList(AiRequestLogBo bo) {
        LambdaQueryWrapper<AiRequestLog> lqw = buildQueryWrapper(bo);
        return baseMapper.list(bo);
    }

    private LambdaQueryWrapper<AiRequestLog> buildQueryWrapper(AiRequestLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiRequestLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, AiRequestLog::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getKey()), AiRequestLog::getKey, bo.getKey());
        lqw.eq(bo.getWordCount() != null, AiRequestLog::getWordCount, bo.getWordCount());
        lqw.eq(bo.getRequestTime() != null, AiRequestLog::getRequestTime, bo.getRequestTime());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), AiRequestLog::getModel, bo.getModel());
        lqw.eq(StringUtils.isNotBlank(bo.getErrorMessage()), AiRequestLog::getErrorMessage, bo.getErrorMessage());
        lqw.eq(StringUtils.isNotBlank(bo.getSendMessage()), AiRequestLog::getSendMessage, bo.getSendMessage());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiveMessage()), AiRequestLog::getReceiveMessage, bo.getReceiveMessage());
        lqw.eq(bo.getPromptId() != null, AiRequestLog::getPromptId, bo.getPromptId());
        return lqw;
    }

    /**
     * 新增请求记录
     */
    @Override
    public Boolean insertByBo(AiRequestLogBo bo) {
        AiRequestLog add = BeanUtil.toBean(bo, AiRequestLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUuid(add.getUuid());
        }
        return flag;
    }

    /**
     * 修改请求记录
     */
    @Override
    public Boolean updateByBo(AiRequestLogBo bo) {
        AiRequestLog update = BeanUtil.toBean(bo, AiRequestLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiRequestLog entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除请求记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean insertOrUpdate(AiRequestLog aiRequestLog) {
        return baseMapper.insertOrUpdate(aiRequestLog);
    }

    @Override
    public TableDataInfo<AiRequestLogVo> page(AiRequestLogBo param, PageQuery pageQuery) {
        Page<AiRequestLogVo> result = baseMapper.page(pageQuery.build(), param);
        return TableDataInfo.build(result);
    }
}
