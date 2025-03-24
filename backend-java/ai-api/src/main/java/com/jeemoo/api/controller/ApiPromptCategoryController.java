package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtflys.forest.annotation.Post;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.AiPrompt;
import com.jeemoo.system.domain.bo.AiPromptCategoryBo;
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.service.IAiPromptCategoryService;
import com.jeemoo.system.service.IAiPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/promptCategory")
public class ApiPromptCategoryController {

    private final IAiPromptCategoryService promptCategoryService;
    private final IAiPromptService promptService;

    @Log(title = "创建提示器分类", businessType = BusinessType.API)
    @PostMapping("/create")
    public R<Object> create(@RequestBody @Validated(AddGroup.class) AiPromptCategoryBo bo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        bo.setCompanyId(loginUser.getCompanyId());
        if (promptCategoryService.insertByBo(bo)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "修改提示器分类", businessType = BusinessType.API)
    @PostMapping("/update")
    public R<Object> update(@RequestBody @Validated(EditGroup.class) AiPromptCategoryBo bo) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiPromptCategoryVo category = promptCategoryService.queryById(bo.getCategoryId());
        if (!category.getCompanyId().equals(loginUser.getCompanyId())) {
            return R.fail("非本企业分类不可修改");
        }
        bo.setCompanyId(loginUser.getCompanyId());
        if (promptCategoryService.updateByBo(bo)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "提示器分类列表", businessType = BusinessType.API)
    @GetMapping("/list")
    public TableDataInfo<AiPromptCategoryVo> list(String keywords, PageQuery pageQuery) {
        AiPromptCategoryBo bo = new AiPromptCategoryBo();
        LoginUser loginUser = LoginHelper.getLoginUser();
        bo.setCompanyId(loginUser.getCompanyId());
        bo.setKeywords(keywords);
        return promptCategoryService.queryPageList(bo, pageQuery);
    }

    @Log(title = "删除提示器分类", businessType = BusinessType.API)
    @DeleteMapping("")
    public R<Object> delete(Long categoryId) {
        AiPromptCategoryVo category = promptCategoryService.queryById(categoryId);
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (category!= null && !Objects.equals(category.getCompanyId(), loginUser.getCompanyId())) {
            return R.fail();
        }

        long count = promptService.count(new QueryWrapper<AiPrompt>().eq("category_id",categoryId));
        if (count > 0) {
            return R.fail("分类下还有"+count+"条数据，不可删除。");
        }

        if (promptCategoryService.deleteById(categoryId)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }
}
