package com.jeemoo.api.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeemoo.api.mapper.HomeMapper;
import com.jeemoo.common.config.AiConfig;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.param.AiModels;
import com.jeemoo.system.param.PromptListResponse;
import com.jeemoo.system.param.PromptSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private AiConfig aiConfig;

    public List<PromptListResponse> recommendList(LoginUser loginUser) {
        return homeMapper.recommendList(loginUser);
    }

    public List<AiPromptCategoryVo> categoryList() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return homeMapper.categoryList(loginUser);
    }

    public TableDataInfo<PromptListResponse> promptPage(PromptSearchParam param, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        Page<PromptListResponse> result = homeMapper.promptPage(pageQuery.build(), param, loginUser.getUserId());
        return TableDataInfo.build(result);
    }

    public List<PromptListResponse> recent(LoginUser loginUser, String platform) {

        if (platform.equals("pc")) {
            List<PromptListResponse> result = homeMapper.recent(loginUser.getUserId(), loginUser.getCompanyId());
            if (result.size() < 14) {
                List<PromptListResponse> recommendList = homeMapper.recommendList(loginUser);
                result.addAll(recommendList);
            }

            result = result.stream().distinct().collect(Collectors.toList());

            return result.subList(0, Math.min(result.size(), 14));
        } else {
            return homeMapper.recent(loginUser.getUserId(), loginUser.getCompanyId());
        }
    }

    public List<JSONObject> models() {
        List<AiModels> list = homeMapper.models();
        HashMap<String, List<AiModels>> map = new HashMap<>();
        for (AiModels aiModels : list) {
            List<AiModels> item = map.get(aiModels.getPlatformName());
            if (item == null) {
                item = new ArrayList<>();
            }
            aiModels.setIcon(aiConfig.getPreviewUrl() + "/static/avatar.png");
            item.add(aiModels);
            map.put(aiModels.getPlatformName(), item);
        }

        List<JSONObject> result = new ArrayList<>();
        for (String key : map.keySet()) {
            JSONObject item = new JSONObject();
            item.set("platformName", key);
            item.set("models", map.get(key));
            result.add(item);
        }

        return result;
    }

    public List<JSONObject> repositoryRecommend() {
        return homeMapper.repositoryRecommend();
    }

    public List<AiPromptCategoryVo> miniappCategoryList() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return homeMapper.miniappCategoryList(loginUser);
    }

    public List<AiModels> modelList() {
        return homeMapper.models();
    }
}
