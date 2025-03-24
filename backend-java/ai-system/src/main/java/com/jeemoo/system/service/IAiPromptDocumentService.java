package com.jeemoo.system.service;

import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiPrompt;
import com.jeemoo.system.domain.AiPromptDocument;
import com.jeemoo.system.domain.vo.AiPromptDocumentVo;
import com.jeemoo.system.domain.bo.AiPromptDocumentBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 提示器文档Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiPromptDocumentService {

    /**
     * 查询提示器文档
     */
    AiPromptDocumentVo queryById(Long id);

    /**
     * 查询提示器文档列表
     */
    TableDataInfo<AiPromptDocumentVo> queryPageList(AiPromptDocumentBo bo, PageQuery pageQuery);

    /**
     * 查询提示器文档列表
     */
    List<AiPromptDocumentVo> queryList(AiPromptDocumentBo bo);

    /**
     * 新增提示器文档
     */
    Boolean insertByBo(AiPromptDocumentBo bo);

    /**
     * 修改提示器文档
     */
    Boolean updateByBo(AiPromptDocumentBo bo);

    /**
     * 校验并批量删除提示器文档信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<Long> fetchDocumentIds(AiPrompt prompt, LoginUser loginUser);
}
