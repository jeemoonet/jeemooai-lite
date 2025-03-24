package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtflys.forest.annotation.Request;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.entity.SysRole;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.AiDocument;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.system.domain.bo.AiPromptCategoryBo;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.param.MenuTree;
import com.jeemoo.system.service.IAiCompanyService;
import com.jeemoo.system.service.IAiDocumentCategoryService;
import com.jeemoo.system.service.IAiDocumentService;
import com.jeemoo.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jeemoo.system.param.DocumentCategoryRoleParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documentCategory")
public class ApiDocumentCategoryController {

    private final IAiDocumentCategoryService documentCategoryService;
    private final IAiDocumentService documentService;
    private final IAiCompanyService companyService;
    private final ISysRoleService roleService;

    @Log(title = "知识库分类列表", businessType = BusinessType.API)
    @GetMapping("/categoryList")
    public R<Object> treeList() {
        List<AiDocumentCategoryVo> result = new ArrayList<>();
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiCompanyVo company = companyService.queryById(loginUser.getCompanyId());
        AiDocumentCategoryBo bo = new AiDocumentCategoryBo();
        bo.setUserId(loginUser.getUserId());
        bo.setCompanyId(loginUser.getCompanyId());
        List<MenuTree> menuTree = documentCategoryService.menuTree(bo,loginUser,company);
        List<MenuTree> myTree = documentCategoryService.myTree(bo, loginUser, company);
        menuTree.addAll(myTree);
        List<Long> categoryIds = new ArrayList<>();
        for (MenuTree tree : menuTree) {
            categoryIds.add(tree.getId());
        }
        if (categoryIds.isEmpty()) {
            return R.ok(result);
        }
        List<AiDocumentCategoryVo> list = documentCategoryService.listByIds(categoryIds);
        List<AiDocumentCategoryVo> tree = AiDocumentCategoryVo.buildCategoryTree(list);
        AiDocumentCategoryVo myCategory = new AiDocumentCategoryVo();
        myCategory.setCategoryId(1L);
        myCategory.setCategoryName("我的知识库");
        myCategory.setDisabled(true);
        AiDocumentCategoryVo companyCategory = new AiDocumentCategoryVo();
        companyCategory.setCategoryId(2L);
        companyCategory.setCategoryName("共享知识库");
        companyCategory.setDisabled(true);

        for (AiDocumentCategoryVo aiDocumentCategoryVo : tree) {
            if (aiDocumentCategoryVo.getIsMy() == 1) {
                aiDocumentCategoryVo.setParentId(1L);
                myCategory.addChild(aiDocumentCategoryVo);
            } else {
                companyCategory.setParentId(2L);
                companyCategory.addChild(aiDocumentCategoryVo);
            }
        }

        result.add(myCategory);
        result.add(companyCategory);
        return R.ok(result);
    }

    @Log(title = "知识库分类列表", businessType = BusinessType.API)
    @GetMapping("/tree")
    public R<Object> tree() {
        return this.treeList();
    }

    @Log(title = "知识库分类列表", businessType = BusinessType.API)
    @GetMapping("/list")
    public TableDataInfo<AiDocumentCategoryVo> list(PageQuery pageQuery) {
        AiDocumentCategoryBo bo = new AiDocumentCategoryBo();
        LoginUser loginUser = LoginHelper.getLoginUser();
        bo.setCompanyId(loginUser.getCompanyId());
        return documentCategoryService.queryPageList(bo,pageQuery);
    }

    @Log(title = "新建知识库分类", businessType = BusinessType.API)
    @PostMapping("/create")
    @Transactional
    public R<Object> create(@RequestBody @Validated(AddGroup.class) AiDocumentCategoryBo bo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        bo.setCompanyId(loginUser.getCompanyId());
        if (documentCategoryService.insertByBo(bo)) {
            DocumentCategoryRoleParam param = new DocumentCategoryRoleParam();
            param.setCategoryId(bo.getCategoryId());
            param.setRoleIds(bo.getRoleIds());
            this.documentCategoryService.setRole(param);
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "修改提示器分类", businessType = BusinessType.API)
    @PostMapping("/update")
    @Transactional
    public R<Object> update(@RequestBody @Validated(EditGroup.class) AiDocumentCategoryBo bo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentCategoryVo category = documentCategoryService.queryById(bo.getCategoryId());
        if (!category.getCompanyId().equals(loginUser.getCompanyId())) {
            return R.fail("非本企业分类不可修改");
        }
        bo.setCompanyId(loginUser.getCompanyId());
        if (documentCategoryService.updateByBo(bo)) {
            DocumentCategoryRoleParam param = new DocumentCategoryRoleParam();
            param.setCategoryId(bo.getCategoryId());
            param.setRoleIds(bo.getRoleIds());
            this.documentCategoryService.setRole(param);
            return R.ok();
        } else {
            return R.fail();
        }
    }


    @Log(title = "删除提示器分类", businessType = BusinessType.API)
    @DeleteMapping("")
    public R<Object> delete(Long categoryId) {
        AiDocumentCategoryVo category = documentCategoryService.queryById(categoryId);
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (category!= null && !Objects.equals(category.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail();
        }

        long count = documentService.count(new QueryWrapper<AiDocument>().eq("category_id",categoryId));
        if (count > 0) {
            return R.fail("分类下还有"+count+"条数据，不可删除。");
        }


        if (documentCategoryService.deleteById(categoryId)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "知识库分类关联角色", businessType = BusinessType.API)
    @PostMapping("/role")
    public R<Object> role(@RequestBody DocumentCategoryRoleParam param) {
        if (documentCategoryService.setRole(param)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "知识库分类关联角色", businessType = BusinessType.API)
    @GetMapping("/role")
    public R<Object> getRole(Long categoryId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<SysRole> select = new ArrayList<>();
        if (categoryId != null && categoryId != 0) {
            select = documentCategoryService.getRole(categoryId);
        }
        SysRole role = new SysRole();
        role.setCompanyId(loginUser.getCompanyId());
        List<SysRole> list = roleService.selectRoleList(role);

        JSONObject result = new JSONObject();
        result.set("selected",select);
        result.set("list",list);
        return R.ok(result);
    }
}
