package com.jeemoo.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.entity.SysRole;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiDocumentCategory;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.system.param.DocumentCategoryRoleParam;
import com.jeemoo.system.param.MenuTree;

import java.util.Collection;
import java.util.List;

/**
 * 知识库分类Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiDocumentCategoryService {

    /**
     * 查询知识库分类
     */
    AiDocumentCategoryVo queryById(Long categoryId);

    /**
     * 查询知识库分类列表
     */
    TableDataInfo<AiDocumentCategoryVo> queryPageList(AiDocumentCategoryBo bo, PageQuery pageQuery);

    /**
     * 查询知识库分类列表
     */
    List<AiDocumentCategoryVo> queryList(AiDocumentCategoryBo bo);

    /**
     * 新增知识库分类
     */
    Boolean insertByBo(AiDocumentCategoryBo bo);

    /**
     * 修改知识库分类
     */
    Boolean updateByBo(AiDocumentCategoryBo bo);

    /**
     * 校验并批量删除知识库分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<MenuTree> menuTree(AiDocumentCategoryBo bo, LoginUser loginUser, AiCompanyVo companyVo);

    List<AiDocumentCategoryVo> tree(AiDocumentCategoryBo bo, LoginUser loginUser, AiCompanyVo companyVo);
    List<MenuTree> myTree(AiDocumentCategoryBo bo, LoginUser loginUser, AiCompanyVo companyVo);

    boolean deleteById(Long categoryId);

    List<Long> findDocumentIds(List<String> categoryIds, Long companyId);

    List<AiDocumentCategory> list(QueryWrapper<AiDocumentCategory> queryWrapper);

    void insertBatch(List<AiDocumentCategory> documentCategoryList);

    void updateBatch(List<AiDocumentCategory> documentCategoryList);

    boolean setRole(DocumentCategoryRoleParam param);

    List<SysRole> getRole(Long categoryId);

    List<AiDocumentCategoryVo> listByIds(List<Long> categoryIds);

    void updatePathByCompanyId(Long companyId);
}
