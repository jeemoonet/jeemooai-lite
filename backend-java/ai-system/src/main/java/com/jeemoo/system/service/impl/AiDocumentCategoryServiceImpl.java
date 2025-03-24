package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.entity.SysRole;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.domain.AiDocument;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.mapper.AiDocumentMapper;
import com.jeemoo.system.param.DocumentCategoryRoleParam;
import com.jeemoo.system.param.MenuTree;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.system.domain.AiDocumentCategory;
import com.jeemoo.system.mapper.AiDocumentCategoryMapper;
import com.jeemoo.system.service.IAiDocumentCategoryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 知识库分类Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiDocumentCategoryServiceImpl implements IAiDocumentCategoryService {

    private final AiDocumentCategoryMapper baseMapper;
    private final AiDocumentMapper documentMapper;

    /**
     * 查询知识库分类
     */
    @Override
    public AiDocumentCategoryVo queryById(Long categoryId){
        return baseMapper.selectVoById(categoryId);
    }

    /**
     * 查询知识库分类列表
     */
    @Override
    public TableDataInfo<AiDocumentCategoryVo> queryPageList(AiDocumentCategoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiDocumentCategory> lqw = buildQueryWrapper(bo);
        Page<AiDocumentCategoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询知识库分类列表
     */
    @Override
    public List<AiDocumentCategoryVo> queryList(AiDocumentCategoryBo bo) {
        LambdaQueryWrapper<AiDocumentCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiDocumentCategory> buildQueryWrapper(AiDocumentCategoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiDocumentCategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCategoryName()), AiDocumentCategory::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryIcon()), AiDocumentCategory::getCategoryIcon, bo.getCategoryIcon());
        lqw.eq(bo.getParentId() != null, AiDocumentCategory::getParentId, bo.getParentId());
        lqw.eq(bo.getCompanyId() != null, AiDocumentCategory::getCompanyId, bo.getCompanyId());
        return lqw;
    }

    /**
     * 新增知识库分类
     */
    @Override
    public Boolean insertByBo(AiDocumentCategoryBo bo) {
        AiDocumentCategory add = BeanUtil.toBean(bo, AiDocumentCategory.class);
        add.setPath("");
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setCategoryId(add.getCategoryId());
            if (bo.getParentId() != null && bo.getParentId() != 0L) {
                AiDocumentCategory parent = baseMapper.selectById(add.getParentId());
                String parentPath = "";
                if (parent == null) {
                    parentPath = "0,";
                } else {
                    parentPath = parent.getPath();
                }
                add.setPath(parentPath + add.getCategoryId() + ",");
            } else {
                add.setPath("0,"+add.getCategoryId()+",");
            }
            baseMapper.updateById(add);
        }
        return flag;
    }

    /**
     * 修改知识库分类
     */
    @Override
    public Boolean updateByBo(AiDocumentCategoryBo bo) {
        AiDocumentCategory update = BeanUtil.toBean(bo, AiDocumentCategory.class);
        update.setPath("");
        validEntityBeforeSave(update);
        if (baseMapper.updateById(update) > 0) {
            bo.setCategoryId(update.getCategoryId());
            if (bo.getParentId() != null && bo.getParentId() != 0L) {
                AiDocumentCategory parent = baseMapper.selectById(update.getParentId());
                if (parent != null) {
                    update.setPath(parent.getPath() + update.getCategoryId() + ",");
                } else {
                    update.setPath("0,"+update.getCategoryId()+",");
                }
            } else {
                update.setPath("0,"+update.getCategoryId()+",");
            }
            return baseMapper.updateById(update) > 0;
        } else {
            return false;
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiDocumentCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除知识库分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<MenuTree> menuTree(AiDocumentCategoryBo bo, LoginUser loginUser, AiCompanyVo companyVo) {
        List<Long> ids = null;
        if (!Objects.equals(loginUser.getUserId(), companyVo.getUserId())) {
            List<AiDocumentCategoryVo> list = baseMapper.treeByRole(bo,loginUser.getUserId());
            if (!list.isEmpty()) {
                ids = new ArrayList<>();
                for (AiDocumentCategoryVo aiDocumentCategoryVo : list) {
                    ids.add(aiDocumentCategoryVo.getCategoryId());
                }
            } else {
                return new ArrayList<>();
            }
        }
        return baseMapper.menuTree(bo, ids);
    }

    @Override
    public List<AiDocumentCategoryVo> tree(AiDocumentCategoryBo bo, LoginUser loginUser, AiCompanyVo companyVo) {
        return baseMapper.tree(bo);
    }

    @Override
    public List<MenuTree> myTree(AiDocumentCategoryBo bo, LoginUser loginUser, AiCompanyVo companyVo) {
        Long count = baseMapper.selectCount(new QueryWrapper<AiDocumentCategory>().eq("user_id", loginUser.getUserId()).eq("is_my", 1));
        if (count == 0) {
            AiDocumentCategory defaultCategory = new AiDocumentCategory();
            defaultCategory.setUserId(loginUser.getUserId());
            defaultCategory.setCompanyId(loginUser.getCompanyId());
            defaultCategory.setCategoryName("默认分类");
            defaultCategory.setIsMy(1);
            defaultCategory.setPath("");
            baseMapper.insert(defaultCategory);
            defaultCategory.setPath("0,"+defaultCategory.getCategoryId());
            baseMapper.updateById(defaultCategory);
        }
        return baseMapper.myMenuTree(bo);
    }

    @Override
    public boolean deleteById(Long categoryId) {
        return baseMapper.deleteById(categoryId) > 0;
    }

    @Override
    public List<Long> findDocumentIds(List<String> categoryIds, Long companyId) {
        List<Long> documentIds = new ArrayList<>();
        if (categoryIds != null && categoryIds.size() > 0) {
            List<AiDocumentCategory> documentCategories = baseMapper
                    .selectList(new QueryWrapper<AiDocumentCategory>()
                            .eq("company_id",companyId));

            Set<Long> ids = new HashSet<>();
            for (AiDocumentCategory documentCategory : documentCategories) {
                String pathStr = documentCategory.getPath();
                String[] path = pathStr.split(",");
                for (String docCategoryId : categoryIds) {
                    if (path.length > 0) {
                        for (String s : path) {
                            if (s.equals(docCategoryId)) {
                                ids.add(documentCategory.getCategoryId());
                                break;
                            }
                        }
                    }
                }
            }
            if (ids.size() > 0) {
                List<AiDocument> documents = documentMapper.selectList(new QueryWrapper<AiDocument>()
                        .eq("status",2).in("category_id", ids));
                if (documents.size() > 0) {
                    for (AiDocument document : documents) {
                        documentIds.add(document.getDocumentId());
                    }
                }
            }
        }

        return documentIds;
    }

    @Override
    public List<AiDocumentCategory> list(QueryWrapper<AiDocumentCategory> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void insertBatch(List<AiDocumentCategory> documentCategoryList) {
        baseMapper.insertBatch(documentCategoryList);
    }

    @Override
    public void updateBatch(List<AiDocumentCategory> documentCategoryList) {
        baseMapper.updateBatchById(documentCategoryList);
    }

    @Override
    @Transactional
    public boolean setRole(DocumentCategoryRoleParam param) {
        baseMapper.deleteRole(param.getCategoryId());
        if (param.getRoleIds().isEmpty()) {
            return true;
        }
        return baseMapper.setRole(param.getCategoryId(),param.getRoleIds()) > 0;
    }

    @Override
    public List<SysRole> getRole(Long categoryId) {
        return baseMapper.getRole(categoryId);
    }

    @Override
    public List<AiDocumentCategoryVo> listByIds(List<Long> categoryIds) {
        return baseMapper.selectVoList(new QueryWrapper<AiDocumentCategory>().in("category_id",categoryIds));
    }

    @Override
    public void updatePathByCompanyId(Long companyId) {
        baseMapper.updatePathByCompanyId(companyId);
    }
}
