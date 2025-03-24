package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.service.ConfigService;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.domain.AiRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiRecordFileBo;
import com.jeemoo.system.domain.vo.AiRecordFileVo;
import com.jeemoo.system.domain.AiRecordFile;
import com.jeemoo.system.mapper.AiRecordFileMapper;
import com.jeemoo.system.service.IAiRecordFileService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 对话文件Service业务层处理
 *
 * @author ai
 * @date 2024-05-16
 */
@RequiredArgsConstructor
@Service
public class AiRecordFileServiceImpl implements IAiRecordFileService {

    private final AiRecordFileMapper baseMapper;
    private final ConfigService configService;
    /**
     * 查询对话文件
     */
    @Override
    public AiRecordFileVo queryById(String uuid){
        return baseMapper.selectVoById(uuid);
    }

    /**
     * 查询对话文件列表
     */
    @Override
    public TableDataInfo<AiRecordFileVo> queryPageList(AiRecordFileBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiRecordFile> lqw = buildQueryWrapper(bo);
        Page<AiRecordFileVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询对话文件列表
     */
    @Override
    public List<AiRecordFileVo> queryList(AiRecordFileBo bo) {
        LambdaQueryWrapper<AiRecordFile> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiRecordFile> buildQueryWrapper(AiRecordFileBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiRecordFile> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), AiRecordFile::getFileName, bo.getFileName());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), AiRecordFile::getIcon, bo.getIcon());
        lqw.eq(bo.getFileSize() != null, AiRecordFile::getFileSize, bo.getFileSize());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), AiRecordFile::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getFileType()), AiRecordFile::getFileType, bo.getFileType());
        lqw.eq(StringUtils.isNotBlank(bo.getFileUrl()), AiRecordFile::getFileUrl, bo.getFileUrl());
        lqw.eq(bo.getRecordId() != null, AiRecordFile::getRecordId, bo.getRecordId());
        return lqw;
    }

    /**
     * 新增对话文件
     */
    @Override
    public Boolean insertByBo(AiRecordFileBo bo) {
        AiRecordFile add = BeanUtil.toBean(bo, AiRecordFile.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUuid(add.getUuid());
        }
        return flag;
    }

    /**
     * 修改对话文件
     */
    @Override
    public Boolean updateByBo(AiRecordFileBo bo) {
        AiRecordFile update = BeanUtil.toBean(bo, AiRecordFile.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiRecordFile entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除对话文件
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean insertBatch(List<AiRecordFile> fileList) {
        return baseMapper.insertBatch(fileList);
    }

    @Override
    public String getFileContent(List<String> filesUuid) {
        String filePrompt = configService.getConfigValue("file_prompt");
        List<AiRecordFile> list = baseMapper.selectList(new QueryWrapper<AiRecordFile>().in("uuid", filesUuid));
        StringBuilder content = new StringBuilder();
        for (AiRecordFile recordFile : list) {
            content.append(filePrompt.replace("{file_name}", recordFile.getFileName()).replace("{file_content}",recordFile.getContent()));
            content.append("\n\n");
        }
        return content.toString();
    }

    @Override
    public void updateRecordId(AiRecord record, List<String> filesUuid) {
        baseMapper.updateRecordId(record.getMessageId(), filesUuid);
    }

    @Override
    public List<AiRecordFile> getFileByRecordIds(List<Long> recordIds) {
        return baseMapper.getFileByRecordIds(recordIds);
    }
}
