package com.jeemoo.system.service;

import com.jeemoo.system.domain.AiPromptTips;
import com.jeemoo.system.domain.vo.AiPromptTipsVo;
import com.jeemoo.system.domain.bo.AiPromptTipsBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 提示器常用语Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiPromptTipsService {

    /**
     * 查询提示器常用语
     */
    AiPromptTipsVo queryById(Long id);

    /**
     * 查询提示器常用语列表
     */
    TableDataInfo<AiPromptTipsVo> queryPageList(AiPromptTipsBo bo, PageQuery pageQuery);

    /**
     * 查询提示器常用语列表
     */
    List<AiPromptTipsVo> queryList(AiPromptTipsBo bo);

    /**
     * 新增提示器常用语
     */
    Boolean insertByBo(AiPromptTipsBo bo);

    /**
     * 修改提示器常用语
     */
    Boolean updateByBo(AiPromptTipsBo bo);

    /**
     * 校验并批量删除提示器常用语信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean saveBatch(List<AiPromptTips> tipsSaveBatch);
}
