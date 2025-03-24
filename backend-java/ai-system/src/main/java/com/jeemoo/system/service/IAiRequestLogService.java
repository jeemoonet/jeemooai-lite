package com.jeemoo.system.service;

import com.jeemoo.system.domain.AiRequestLog;
import com.jeemoo.system.domain.vo.AiRequestLogVo;
import com.jeemoo.system.domain.bo.AiRequestLogBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 请求记录Service接口
 *
 * @author ai
 * @date 2023-09-12
 */
public interface IAiRequestLogService {

    /**
     * 查询请求记录
     */
    AiRequestLogVo queryById(String uuid);

    /**
     * 查询请求记录列表
     */
    TableDataInfo<AiRequestLogVo> queryPageList(AiRequestLogBo bo, PageQuery pageQuery);

    /**
     * 查询请求记录列表
     */
    List<AiRequestLogVo> queryList(AiRequestLogBo bo);

    /**
     * 新增请求记录
     */
    Boolean insertByBo(AiRequestLogBo bo);

    /**
     * 修改请求记录
     */
    Boolean updateByBo(AiRequestLogBo bo);

    /**
     * 校验并批量删除请求记录信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    Boolean insertOrUpdate(AiRequestLog aiRequestLog);

    TableDataInfo<AiRequestLogVo> page(AiRequestLogBo param, PageQuery pageQuery);

}
