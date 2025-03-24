package com.jeemoo.system.service;

import com.jeemoo.system.domain.AiRecord;
import com.jeemoo.system.domain.AiRecordFile;
import com.jeemoo.system.domain.vo.AiRecordFileVo;
import com.jeemoo.system.domain.bo.AiRecordFileBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 对话文件Service接口
 *
 * @author ai
 * @date 2024-05-16
 */
public interface IAiRecordFileService {

    /**
     * 查询对话文件
     */
    AiRecordFileVo queryById(String uuid);

    /**
     * 查询对话文件列表
     */
    TableDataInfo<AiRecordFileVo> queryPageList(AiRecordFileBo bo, PageQuery pageQuery);

    /**
     * 查询对话文件列表
     */
    List<AiRecordFileVo> queryList(AiRecordFileBo bo);

    /**
     * 新增对话文件
     */
    Boolean insertByBo(AiRecordFileBo bo);

    /**
     * 修改对话文件
     */
    Boolean updateByBo(AiRecordFileBo bo);

    /**
     * 校验并批量删除对话文件信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    boolean insertBatch(List<AiRecordFile> fileList);

    String getFileContent(List<String> filesUuid);

    void updateRecordId(AiRecord record, List<String> filesUuid);

    List<AiRecordFile> getFileByRecordIds(List<Long> recordIds);
}
