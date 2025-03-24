package com.jeemoo.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.system.domain.AiPromptCategory;
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.domain.bo.AiPromptCategoryBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 提示器分类Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiPromptCategoryService {

    /**
     * 查询提示器分类
     */
    AiPromptCategoryVo queryById(Long categoryId);

    /**
     * 查询提示器分类列表
     */
    TableDataInfo<AiPromptCategoryVo> queryPageList(AiPromptCategoryBo bo, PageQuery pageQuery);

    /**
     * 查询提示器分类列表
     */
    List<AiPromptCategoryVo> queryList(AiPromptCategoryBo bo);

    /**
     * 新增提示器分类
     */
    Boolean insertByBo(AiPromptCategoryBo bo);

    /**
     * 修改提示器分类
     */
    Boolean updateByBo(AiPromptCategoryBo bo);

    /**
     * 校验并批量删除提示器分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean deleteById(Long categoryId);

    List<AiPromptCategory> list(QueryWrapper<AiPromptCategory> queryWrapper);

    void insert(AiPromptCategory aiPromptCategory);
}
