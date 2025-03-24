package com.jeemoo.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiDocument;
import com.jeemoo.system.domain.vo.AiDocumentVo;
import com.jeemoo.system.domain.bo.AiDocumentBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.system.param.DocumentFolderSearchParam;
import com.jeemoo.system.param.SplitDocumentParam;

import java.util.Collection;
import java.util.List;

/**
 * 文档Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiDocumentService {

    /**
     * 查询文档
     */
    AiDocumentVo queryById(Long documentId);

    /**
     * 查询文档列表
     */
    TableDataInfo<AiDocumentVo> queryPageList(AiDocumentBo bo, PageQuery pageQuery);

    /**
     * 查询文档列表
     */
    List<AiDocumentVo> queryList(AiDocumentBo bo);

    /**
     * 新增文档
     */
    Boolean insertByBo(AiDocumentBo bo);

    /**
     * 修改文档
     */
    Boolean updateByBo(AiDocumentBo bo);

    /**
     * 校验并批量删除文档信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<AiDocumentVo> apiPageList(AiDocumentBo param, PageQuery pageQuery, LoginUser loginUser);

    boolean deleteById(Long documentId, LoginUser loginUser);

    long count(QueryWrapper<AiDocument> queryWrapper);

    boolean setStatus(Long documentId, int i);

    boolean split(Long documentId, SplitDocumentParam param);


    List<AiDocument> select(QueryWrapper<AiDocument> queryWrapper);

    List<AiDocumentVo> folder(DocumentFolderSearchParam param, LoginUser loginUser);
}
