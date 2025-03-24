package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.exception.ServiceException;
import com.jeemoo.common.exception.biz.DocumentBaseFullException;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.domain.AiCompany;
import com.jeemoo.system.domain.AiDocumentCategory;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.system.mapper.AiCompanyMapper;
import com.jeemoo.system.mapper.AiDocumentCategoryMapper;
import com.jeemoo.system.param.DocumentFolderSearchParam;
import com.jeemoo.system.param.SplitDocumentParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiDocumentBo;
import com.jeemoo.system.domain.vo.AiDocumentVo;
import com.jeemoo.system.domain.AiDocument;
import com.jeemoo.system.mapper.AiDocumentMapper;
import com.jeemoo.system.service.IAiDocumentService;

import java.util.*;

/**
 * 文档Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiDocumentServiceImpl implements IAiDocumentService {

    private final AiDocumentMapper baseMapper;
    private final AiCompanyMapper companyMapper;
    private final AiDocumentCategoryMapper documentCategoryMapper;

    /**
     * 查询文档
     */
    @Override
    public AiDocumentVo queryById(Long documentId){
        return baseMapper.selectVoById(documentId);
    }

    /**
     * 查询文档列表
     */
    @Override
    public TableDataInfo<AiDocumentVo> queryPageList(AiDocumentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiDocument> lqw = buildQueryWrapper(bo);
        Page<AiDocumentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询文档列表
     */
    @Override
    public List<AiDocumentVo> queryList(AiDocumentBo bo) {
        LambdaQueryWrapper<AiDocument> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiDocument> buildQueryWrapper(AiDocumentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiDocument> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCategoryId()!=null && bo.getCategoryId()!=0L,AiDocument::getCategoryId,bo.getCategoryId());
        lqw.eq(StringUtils.isNotBlank(bo.getDocumentType()), AiDocument::getDocumentType, bo.getDocumentType());
        lqw.like(StringUtils.isNotBlank(bo.getDocumentName()), AiDocument::getDocumentName, bo.getDocumentName());
        lqw.and(StringUtils.isNotBlank(bo.getKeywords()),
                e->{e.like(AiDocument::getDocumentName,bo.getKeywords())
                        .or()
                        .like(AiDocument::getDocumentDesc,bo.getKeywords());
        });
        lqw.eq(StringUtils.isNotBlank(bo.getDocumentDesc()), AiDocument::getDocumentDesc, bo.getDocumentDesc());
        lqw.eq(bo.getFileSize() != null, AiDocument::getFileSize, bo.getFileSize());
        lqw.eq(bo.getStatus() != null && bo.getStatus() != 0, AiDocument::getStatus, bo.getStatus());
        lqw.eq(bo.getPageNumber() != null, AiDocument::getPageNumber, bo.getPageNumber());
        lqw.eq(bo.getCompanyId() != null && bo.getCompanyId() != 0L, AiDocument::getCompanyId, bo.getCompanyId());
        lqw.orderByDesc(AiDocument::getCreateTime);
        return lqw;
    }

    /**
     * 新增文档
     */
    @Override
    public Boolean insertByBo(AiDocumentBo bo) {
        AiDocument add = BeanUtil.toBean(bo, AiDocument.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setDocumentId(add.getDocumentId());
        }
        return flag;
    }

    /**
     * 修改文档
     */
    @Override
    public Boolean updateByBo(AiDocumentBo bo) {
        AiDocument update = BeanUtil.toBean(bo, AiDocument.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiDocument entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除文档
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<AiDocumentVo> apiPageList(AiDocumentBo param, PageQuery pageQuery, LoginUser loginUser) {
        param.setCompanyId(loginUser.getCompanyId());
        Page<AiDocumentVo> list = baseMapper.selectVoPage(pageQuery.build(),buildQueryWrapper(param));
        return TableDataInfo.build(list);
    }

    @Override
    public boolean deleteById(Long documentId, LoginUser loginUser) {
        return baseMapper.delete(new QueryWrapper<AiDocument>()
                .eq("document_id",documentId).eq("company_id",loginUser.getCompanyId())) > 0;
    }

    @Override
    public long count(QueryWrapper<AiDocument> queryWrapper) {
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public boolean setStatus(Long documentId, int i) {
        return baseMapper.setStatus(documentId,i) > 0;
    }

    @Override
    public boolean split(Long documentId, SplitDocumentParam param) {
        return baseMapper.split(documentId,param) > 0;
    }

    @Override
    public List<AiDocument> select(QueryWrapper<AiDocument> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<AiDocumentVo> folder(DocumentFolderSearchParam param, LoginUser loginUser) {
        List<AiDocumentVo> results = new ArrayList<>();
        if (param.getFolderId() == null || param.getFolderId() == 0) {
            AiDocumentVo myCategory = new AiDocumentVo();
            myCategory.setDocumentName("我的知识库");
            myCategory.setFolderId(1L);
            myCategory.setType("folder");
            results.add(myCategory);

            AiDocumentVo companyCategory = new AiDocumentVo();
            companyCategory.setDocumentName("共享知识库");
            companyCategory.setFolderId(2L);
            companyCategory.setType("folder");
            results.add(companyCategory);
        } else {
            List<AiDocumentCategoryVo> categories = new ArrayList<>();
            AiCompany company = companyMapper.selectById(loginUser.getCompanyId());
            AiDocumentCategoryBo documentCategoryBo = new AiDocumentCategoryBo();
            documentCategoryBo.setCompanyId(loginUser.getCompanyId());
            documentCategoryBo.setParentId(param.getFolderId());
            if (param.getFolderId().equals(1L)) {
                categories = documentCategoryMapper.selectVoList(new QueryWrapper<AiDocumentCategory>()
                        .eq("company_id", loginUser.getCompanyId())
                        .eq("parent_id", param.getFolderId())
                        .eq("user_id", loginUser.getUserId()));
            } else if (param.getFolderId().equals(2L)) {
                if (company.getUserId().equals(loginUser.getUserId())) {
                    categories = documentCategoryMapper.selectVoList(new QueryWrapper<AiDocumentCategory>()
                            .eq("company_id", loginUser.getCompanyId())
                            .eq("parent_id", param.getFolderId()));
                } else {
                    categories = documentCategoryMapper.treeByRole(documentCategoryBo, loginUser.getUserId());
                }
            } else {
                AiDocumentCategory category = documentCategoryMapper.selectById(param.getFolderId());
                if (category.getIsMy() == 1) {
                    categories = documentCategoryMapper.selectVoList(new QueryWrapper<AiDocumentCategory>()
                            .eq("company_id", loginUser.getCompanyId())
                            .eq("parent_id", param.getFolderId())
                            .eq("user_id", loginUser.getUserId()));
                } else {
                    if (company.getUserId().equals(loginUser.getUserId())) {
                        categories = documentCategoryMapper.selectVoList(new QueryWrapper<AiDocumentCategory>()
                                .eq("company_id", loginUser.getCompanyId())
                                .eq("parent_id", param.getFolderId()));
                    } else {
                        categories = documentCategoryMapper.treeByRole(documentCategoryBo, loginUser.getUserId());
                    }
                }
            }
            for (AiDocumentCategoryVo category : categories) {
                if (StringUtils.isNotEmpty(param.getKeywords()) && !category.getCategoryName().contains(param.getKeywords())) {
                    continue;
                }
                AiDocumentVo vo = new AiDocumentVo();
                vo.setFolderId(category.getCategoryId());
                vo.setDocumentName(category.getCategoryName());
                vo.setUpdateTime(category.getUpdateTime());
                vo.setType("folder");
                results.add(vo);
            }

            AiDocumentBo documentBo = new AiDocumentBo();
            documentBo.setCompanyId(loginUser.getCompanyId());
            documentBo.setCategoryId(param.getFolderId());
            documentBo.setStatus(param.getStatus());
            documentBo.setKeywords(param.getKeywords());
            List<AiDocumentVo> documentVos = baseMapper.selectVoList(buildQueryWrapper(documentBo));
            for (AiDocumentVo documentVo : documentVos) {
                documentVo.setType("document");
            }
            results.addAll(documentVos);
        }

        return results;
    }
}
