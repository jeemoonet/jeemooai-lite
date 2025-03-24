package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.*;
import com.jeemoo.system.domain.vo.*;
import com.jeemoo.system.mapper.*;
import com.jeemoo.system.param.*;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiPromptBo;
import com.jeemoo.system.service.IAiPromptService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 提示器Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiPromptServiceImpl implements IAiPromptService {

    private final AiPromptMapper baseMapper;
    private final AiPromptTipsMapper promptTipsMapper;
    private final AiPromptCategoryMapper promptCategoryMapper;
    private final AiCompanyMapper companyMapper;

    /**
     * 查询提示器
     */
    @Override
    public AiPromptVo queryById(Long promptId){
        return baseMapper.selectVoById(promptId);
    }

    /**
     * 查询提示器列表
     */
    @Override
    public TableDataInfo<AiPromptVo> queryPageList(AiPromptBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiPrompt> lqw = buildQueryWrapper(bo);
        Page<AiPromptVo> result = baseMapper.listPage(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询提示器列表
     */
    @Override
    public List<JSONObject> options(AiPromptBo bo) {
        return baseMapper.options(bo);
    }

    private LambdaQueryWrapper<AiPrompt> buildQueryWrapper(AiPromptBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiPrompt> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getPromptName()), AiPrompt::getPromptName, bo.getPromptName());
        lqw.eq(StringUtils.isNotBlank(bo.getPromptDesc()), AiPrompt::getPromptDesc, bo.getPromptDesc());
        lqw.eq(bo.getPromptType() != null, AiPrompt::getPromptType, bo.getPromptType());
        lqw.eq(StringUtils.isNotBlank(bo.getPromptIcon()), AiPrompt::getPromptIcon, bo.getPromptIcon());
        lqw.eq(bo.getUserId() != null, AiPrompt::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getInitPrompt()), AiPrompt::getInitPrompt, bo.getInitPrompt());
        lqw.eq(StringUtils.isNotBlank(bo.getInitMessage()), AiPrompt::getInitMessage, bo.getInitMessage());
        lqw.eq(bo.getIsPublic() != null, AiPrompt::getIsPublic, bo.getIsPublic());
        lqw.eq(bo.getUseNum() != null, AiPrompt::getUseNum, bo.getUseNum());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), AiPrompt::getModel, bo.getModel());
        lqw.eq(bo.getMaxTokens() != null, AiPrompt::getMaxTokens, bo.getMaxTokens());
        lqw.eq(bo.getTemperature() != null, AiPrompt::getTemperature, bo.getTemperature());
        lqw.eq(bo.getSortNumber() != null, AiPrompt::getSortNumber, bo.getSortNumber());
        lqw.eq(bo.getCategoryId() != null, AiPrompt::getCategoryId, bo.getCategoryId());
        lqw.eq(bo.getCompanyId() != null && bo.getCompanyId() != 0L, AiPrompt::getCompanyId, bo.getCompanyId());
        return lqw;
    }

    /**
     * 新增提示器
     */
    @Override
    public Boolean insertByBo(AiPromptBo bo) {
        AiPrompt add = BeanUtil.toBean(bo, AiPrompt.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPromptId(add.getPromptId());
        }
        return flag;
    }

    /**
     * 修改提示器
     */
    @Override
    public Boolean updateByBo(AiPromptBo bo) {
        AiPrompt update = BeanUtil.toBean(bo, AiPrompt.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiPrompt entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除提示器
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PromptCreateParam param) throws Exception {
        AiPrompt prompt = BeanUtil.toBean(param, AiPrompt.class);
        prompt.setUserId(param.getLoginUser().getUserId());
        prompt.setCompanyId(param.getLoginUser().getCompanyId());
        prompt.setDocCategoryIds(JSONUtil.toJsonStr(param.getDocCategoryIds()));
        prompt.setIsDoceShow(null);

        baseMapper.insert(prompt);
        param.setPromptId(prompt.getPromptId());

//        List<AiPromptTips> tips = param.getTips();
//        if (tips != null && !tips.isEmpty()) {
//            for (AiPromptTips tip : tips) {
//                if (StrUtil.isEmpty(tip.getTitle())) {
//                    throw new Exception("常用提示语标题不能为空");
//                }
//                if (StrUtil.isEmpty(tip.getMessage())) {
//                    throw new Exception("常用提示语不能为空");
//                }
//                tip.setPromptId(prompt.getPromptId());
//            }
//            promptTipsMapper.insertBatch(tips);
//        }

//        List<AiFunctionPrompt> functionPrompts = param.getFunctions();
//        if (functionPrompts != null && !functionPrompts.isEmpty()) {
//            for (AiFunctionPrompt functionPrompt : functionPrompts) {
//                functionPrompt.setPromptId(prompt.getPromptId());
//            }
//            functionPromptMapper.insertBatch(functionPrompts);
//        }

        return true;
    }


    @Override
    @Transactional
    public boolean update(PromptCreateParam param) throws Exception {
        AiPrompt prompt = BeanUtil.toBean(param, AiPrompt.class);
        prompt.setCompanyId(param.getLoginUser().getCompanyId());
        prompt.setDocCategoryIds(JSONUtil.toJsonStr(param.getDocCategoryIds()));
        prompt.setIsDoceShow(null);

        baseMapper.updateById(prompt);
        param.setPromptId(prompt.getPromptId());

        List<AiPromptTips> tips = param.getTips();
        promptTipsMapper.delete(new QueryWrapper<AiPromptTips>().eq("prompt_id",prompt.getPromptId()));
        if (tips != null && !tips.isEmpty()) {
            for (AiPromptTips tip : tips) {
                tip.setId(null);
                if (StrUtil.isEmpty(tip.getTitle())) {
                    throw new Exception("常用提示语标题不能为空");
                }
                if (StrUtil.isEmpty(tip.getMessage())) {
                    throw new Exception("常用提示语不能为空");
                }
                tip.setPromptId(prompt.getPromptId());
            }
            promptTipsMapper.insertBatch(tips);
        }

        return true;
    }

    @Override
    public boolean deleteById(Long promptId) {
        return baseMapper.deleteById(promptId) > 0;
    }

    @Override
    public AiPromptVo info(Long promptId) {
        AiPromptVo promptVo = this.queryById(promptId);
        promptVo.setDocCategoryIdList(JSONUtil.toList(promptVo.getDocCategoryIds(),Long.class));
        List<AiPromptTipsVo> promptTips = promptTipsMapper.selectVoList(new QueryWrapper<AiPromptTips>().eq("prompt_id", promptId));
        promptVo.setTips(promptTips);
        return promptVo;
    }

    @Override
    public AiPrompt fetchPrompt(AiWindow window, LoginUser loginUser) {
        if (window.getPromptId() == null || window.getPromptId()==0L) {
            return null;
        }

        return baseMapper.selectByIdNoDeleted(window.getPromptId());
    }

    @Override
    public void addUseNum(Long promptId, int num) {
        int i = baseMapper.addUseNum(promptId,num);
    }

    @Override
    public AiPrompt fineById(Long promptId) {
        return baseMapper.selectById(promptId);
    }

    @Override
    public List<AiPrompt> list(QueryWrapper<AiPrompt> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void insertBatch(List<AiPrompt> promptSaveList) {
        baseMapper.insertBatch(promptSaveList);
    }

    @Override
    public long count(QueryWrapper<AiPrompt> queryWrapper) {
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public boolean setStatus(Long promptId, Integer status) {
        return baseMapper.setStatus(promptId, status) > 0;
    }

    @Override
    public TableDataInfo<CompanyPromptResponse> companyPromptPage(PromptSearchParam param, PageQuery pageQuery) {
        Page<CompanyPromptResponse> result = baseMapper.companyPromptPage(pageQuery.build(), param);
        return TableDataInfo.build(result);
    }

    @Override
    public boolean saveBatch(List<AiPrompt> list) {
        return baseMapper.insertBatch(list);
    }

    @Override
    public boolean insert(AiPrompt prompt) {
        return baseMapper.insert(prompt) > 0;
    }

    @Override
    public List<AiPromptVo> queryList(AiPromptBo bo) {
        LambdaQueryWrapper<AiPrompt> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public void recent(AiPrompt prompt, LoginUser loginUser) {
        baseMapper.recent(prompt.getPromptId(),loginUser.getUserId());
    }

    @Override
    public List<ChatPromptSelectResponse> chatSelect(LoginUser loginUser) {
        List<ChatPromptSelectResponse> result = new ArrayList<>();
        List<AiPromptCategory> promptCategoryVoList = promptCategoryMapper.selectList();
        List<PromptListResponse> prompts = baseMapper.chatSelect(loginUser.getUserId(),loginUser.getCompanyId());
        HashMap<Long,List<PromptListResponse>> hashMap = new HashMap<>();
        HashMap<Long, AiPromptCategory> categoryHashMap = new HashMap<>();
        for (PromptListResponse prompt : prompts) {
            List<PromptListResponse> children = hashMap.get(prompt.getCategoryId());
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(prompt);
            hashMap.put(prompt.getCategoryId(),children);
        }
        for (AiPromptCategory aiPromptCategory : promptCategoryVoList) {
            categoryHashMap.put(aiPromptCategory.getCategoryId(),aiPromptCategory);
        }

        for (Long key : hashMap.keySet()) {
            ChatPromptSelectResponse response = new ChatPromptSelectResponse();
            AiPromptCategory category = categoryHashMap.get(key);
            if (category != null) {
                response.setCategoryIcon(category.getCategoryIcon());
                response.setCategoryId(category.getCategoryId());
                response.setCategoryName(category.getCategoryName());
                response.setChildren(hashMap.get(key));
                result.add(response);
            }
        }

        return result;
    }

    @Override
    public List<PromptListResponse> search(String keywords) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return baseMapper.search(loginUser.getCompanyId(), keywords);
    }

    @Override
    public TableDataInfo<PromptListResponse> page(PageQuery page, PromptSearchParam param) {
        Page<PromptListResponse> result = baseMapper.apiPage(page.build(), param);
        return TableDataInfo.build(result);
    }

    @Override
    public boolean copy(PromptCreateParam param) {
        AiPrompt prompt = baseMapper.selectById(param.getPromptId());
        AiPrompt newPrompt = BeanUtil.toBean(prompt, AiPrompt.class);
        newPrompt.setPromptId(null);
        newPrompt.setPromptName(param.getPromptName());
        newPrompt.setPromptIcon(param.getPromptIcon());
        newPrompt.setPromptDesc(param.getPromptDesc());
        newPrompt.setPromptType(param.getPromptType());
        newPrompt.setCompanyId(param.getLoginUser().getCompanyId());
        newPrompt.setUserId(param.getLoginUser().getUserId());
        newPrompt.setIsShare(0);
        newPrompt.setIsDigitPersonShare(0);
        newPrompt.setIsRtcShare(0);
        newPrompt.setIsMinprogramShare(0);
        newPrompt.setIsRobotShare(0);
        newPrompt.setIsPublic(0);
        newPrompt.setStatus(0);
        newPrompt.setCreateBy(param.getLoginUser().getUsername());
        newPrompt.setUpdateBy(param.getLoginUser().getUsername());
        newPrompt.setCreateTime(new Date());
        newPrompt.setUpdateTime(new Date());
        boolean bool = baseMapper.insert(newPrompt) > 0;
        param.setPromptId(newPrompt.getPromptId());
        return bool;
    }

    @Override
    public boolean updateBaseInfo(PromptCreateParam param) {
        AiPrompt prompt = baseMapper.selectById(param.getPromptId());
        prompt.setPromptName(param.getPromptName());
        prompt.setPromptIcon(param.getPromptIcon());
        prompt.setPromptDesc(param.getPromptDesc());
        prompt.setPromptType(param.getPromptType());
        return baseMapper.updateById(prompt) > 0;
    }

    @Override
    public boolean updateById(AiPrompt prompt) {
        return baseMapper.updateById(prompt) > 0;
    }

    @Override
    public List<MenuTree> tree() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<MenuTree> tree = new ArrayList<>();
        List<AiPromptCategory> categoryVos = promptCategoryMapper.selectList(new QueryWrapper<AiPromptCategory>()
                .eq("company_id",loginUser.getCompanyId()));
        AiCompanyVo company = companyMapper.selectVoById(loginUser.getCompanyId());

        QueryWrapper<AiPrompt> where = new QueryWrapper<>();
        where.eq("company_id",loginUser.getCompanyId());
        if (!Objects.equals(company.getUserId(), loginUser.getUserId())) {
            where.eq("user_id",loginUser.getUserId());
        }

        List<AiPrompt> list = baseMapper.selectList(where);

        for (AiPromptCategory categoryVo : categoryVos) {
            MenuTree menu = new MenuTree();
            menu.setId(categoryVo.getCategoryId());
            menu.setMenuType("M");
            menu.setIcon(categoryVo.getCategoryIcon());
            menu.setName(categoryVo.getCategoryName());
            for (AiPrompt prompt : list) {
                if (Objects.equals(prompt.getCategoryId(), categoryVo.getCategoryId())) {
                    MenuTree item = new MenuTree();
                    item.setId(prompt.getPromptId());
                    item.setMenuType("C");
                    item.setIcon(prompt.getPromptIcon());
                    item.setName(prompt.getPromptName());
                    menu.addChild(item);
                }
            }
            tree.add(menu);
        }

        return tree;
    }
}
