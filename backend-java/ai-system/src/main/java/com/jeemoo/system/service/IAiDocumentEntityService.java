package com.jeemoo.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.system.domain.AiDocumentEntity;
import com.jeemoo.system.domain.vo.AiDocumentEntityVo;
import com.jeemoo.system.domain.bo.AiDocumentEntityBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 文档向量关系Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiDocumentEntityService {

    /**
     * 查询文档向量关系
     */
    AiDocumentEntityVo queryById(Long id);

    /**
     * 查询文档向量关系列表
     */
    TableDataInfo<AiDocumentEntityVo> queryPageList(AiDocumentEntityBo bo, PageQuery pageQuery);

    /**
     * 查询文档向量关系列表
     */
    List<AiDocumentEntityVo> queryList(AiDocumentEntityBo bo);

    /**
     * 新增文档向量关系
     */
    Boolean insertByBo(AiDocumentEntityBo bo);

    /**
     * 修改文档向量关系
     */
    Boolean updateByBo(AiDocumentEntityBo bo);

    /**
     * 校验并批量删除文档向量关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<Long> getEntityIds(List<Long> documentIds);

    boolean delete(QueryWrapper<AiDocumentEntity> wrapper);
}
