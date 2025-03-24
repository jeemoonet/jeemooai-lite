package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.AiDocument;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.system.param.MenuTree;
import com.jeemoo.system.service.IAiCompanyService;
import com.jeemoo.system.service.IAiDocumentCategoryService;
import com.jeemoo.system.service.IAiDocumentService;
import com.jeemoo.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/myDocumentCategory")
@RequiredArgsConstructor
public class ApiMyDocumentCategoryController {

    private final IAiDocumentCategoryService documentCategoryService;
    private final IAiDocumentService documentService;
    private final IAiCompanyService companyService;

    @Log(title = "知识库分类列表", businessType = BusinessType.API)
    @GetMapping("/menu")
    public R<Object> treeList() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiCompanyVo company = companyService.queryById(loginUser.getCompanyId());
        AiDocumentCategoryBo bo = new AiDocumentCategoryBo();
        bo.setCompanyId(loginUser.getCompanyId());
        bo.setUserId(loginUser.getUserId());
        List<MenuTree> list = documentCategoryService.myTree(bo,loginUser,company);
        List<MenuTree> tree = MenuTree.buildCategoryTree(list);
        return R.ok(tree);
    }

    @Log(title = "知识库分类列表", businessType = BusinessType.API)
    @GetMapping("/tree")
    public R<Object> tree() {
        return this.treeList();
    }

    @Log(title = "新建知识库分类", businessType = BusinessType.API)
    @PostMapping("/create")
    public R<Object> create(@RequestBody @Validated(AddGroup.class) AiDocumentCategoryBo bo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        bo.setCompanyId(loginUser.getCompanyId());
        bo.setUserId(loginUser.getUserId());
        bo.setIsMy(1);
        bo.setParentId(bo.getParentId());
        if (documentCategoryService.insertByBo(bo)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "修改提示器分类", businessType = BusinessType.API)
    @PostMapping("/update")
    public R<Object> update(@RequestBody @Validated(EditGroup.class) AiDocumentCategoryBo bo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiDocumentCategoryVo category = documentCategoryService.queryById(bo.getCategoryId());
        if (!category.getCompanyId().equals(loginUser.getCompanyId())) {
            return R.fail("非本企业分类不可修改");
        }
        if (!category.getUserId().equals(loginUser.getUserId())) {
            return R.fail("非自己的分类");
        }
        if (category.getIsMy() != 1) {
            return R.fail("只可以修改自己的分类");
        }
        bo.setCompanyId(loginUser.getCompanyId());
        if (documentCategoryService.updateByBo(bo)) {
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
        assert category != null;
        if (category.getIsMy() != 1) {
            return R.fail("只可以删除自己的分类");
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

}
