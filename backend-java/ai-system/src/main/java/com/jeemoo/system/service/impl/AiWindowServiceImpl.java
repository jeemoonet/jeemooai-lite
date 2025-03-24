package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.param.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiWindowBo;
import com.jeemoo.system.domain.vo.AiWindowVo;
import com.jeemoo.system.domain.AiWindow;
import com.jeemoo.system.mapper.AiWindowMapper;
import com.jeemoo.system.service.IAiWindowService;

import java.util.*;

/**
 * 窗口Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiWindowServiceImpl implements IAiWindowService {

    private final AiWindowMapper baseMapper;

    /**
     * 查询窗口
     */
    @Override
    public AiWindowVo queryById(Long windowId){
        return baseMapper.selectVoById(windowId);
    }

    /**
     * 查询窗口列表
     */
    @Override
    public TableDataInfo<AiWindowVo> queryPageList(AiWindowBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiWindow> lqw = buildQueryWrapper(bo);
        Page<AiWindowVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询窗口列表
     */
    @Override
    public List<AiWindowVo> queryList(AiWindowBo bo) {
        LambdaQueryWrapper<AiWindow> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiWindow> buildQueryWrapper(AiWindowBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiWindow> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, AiWindow::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getWindowName()), AiWindow::getWindowName, bo.getWindowName());
        lqw.eq(bo.getTalkType() != null, AiWindow::getTalkType, bo.getTalkType());
        lqw.eq(bo.getPromptId() != null, AiWindow::getPromptId, bo.getPromptId());
        lqw.eq(bo.getDeleted() != null, AiWindow::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增窗口
     */
    @Override
    public Boolean insertByBo(AiWindowBo bo) {
        AiWindow add = BeanUtil.toBean(bo, AiWindow.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setWindowId(add.getWindowId());
        }
        return flag;
    }

    /**
     * 修改窗口
     */
    @Override
    public Boolean updateByBo(AiWindowBo bo) {
        AiWindow update = BeanUtil.toBean(bo, AiWindow.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiWindow entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除窗口
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public AiWindow fetchWindow(SendMessageParam param, LoginUser loginUser, String platform) {
        AiWindow window = null;
        if (param.getWindowId() == null || param.getWindowId()==0L) {
            window = new AiWindow();
            window.setUserId(loginUser.getUserId());
            String windowName = param.getMessage().length() > 20 ? param.getMessage().substring(0, 20) : param.getMessage();
            window.setWindowName(windowName);
            window.setCompanyId(loginUser.getCompanyId());
            window.setPlatform(platform);
            baseMapper.insert(window);
        } else {
            window = baseMapper.selectById(param.getWindowId());
        }

        return window;
    }

    @Override
    public List<WindowListResponse> listByUser(LoginUser loginUser, int talkType, String platform, Long filterWindowId) {
        List<WindowListResponse> list = baseMapper.listByUser(loginUser, talkType, platform, filterWindowId);
        for (WindowListResponse windowListResponse : list) {
            if (windowListResponse.getPromptId() == null || windowListResponse.getPromptId() == 0L) {
                windowListResponse.setPromptName("自由问答");
            }
        }
        return list;
    }

    @Override
    public boolean deleteById(Long windowId) {
        return baseMapper.deleteById(windowId) > 0;
    }

    @Override
    public boolean rename(RenameWindowParam param, LoginUser loginUser) {
        return baseMapper.rename(param,loginUser) > 0;
    }

    @Override
    public AiWindow findOne(Long windowId) {
        return baseMapper.selectById(windowId);
    }

    @Override
    public AiWindow getOne(QueryWrapper<AiWindow> queryWrapper) {
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void insert(AiWindow window) {
        baseMapper.insert(window);
    }

    @Override
    public TableDataInfo<CompanyWindowResponse> pageByCompany(LoginUser loginUser, CompanyWindowParam param, PageQuery pageQuery) {
        Page<CompanyWindowResponse> result = baseMapper.pageByCompany(pageQuery.build(), param,loginUser);
        return TableDataInfo.build(result);
    }

    @Override
    public List<AiWindow> fetchTestWindow(SendTestMessageParam param, LoginUser loginUser, String uuid) {
        List<AiWindow> windows = new ArrayList<>();
        if (StringUtils.isEmpty(param.getUuid())) {
            List<AiWindow> windowTests = new ArrayList<>();
            for (String model : param.getModels()) {
                AiWindow windowTest = new AiWindow();
                windowTest.setUuid(uuid);
                windowTest.setModel(model);
                windowTest.setUserId(loginUser.getUserId());
                windowTest.setCompanyId(loginUser.getCompanyId());
                windowTest.setPromptId(param.getPromptId());
                windowTest.setTalkType(1);
                windowTests.add(windowTest);
            }
            baseMapper.insertBatch(windowTests);
            windows = windowTests;
        } else {
            windows = baseMapper.selectList(new QueryWrapper<AiWindow>().eq("uuid",param.getUuid()).in("model",param.getModels()));
        }
        return windows;
    }

    @Override
    public Long findOneFreeWindow(Long userId) {
        return this.findOneFreeWindow(userId, "miniapp");
    }

    @Override
    public Long findOneFreeWindow(Long userId, String platform) {
        return baseMapper.findOneFreeWindow(userId, platform, null);
    }

    @Override
    public Long findOneFreeWindow(Long userId, String platform, Long promptId) {
        return baseMapper.findOneFreeWindow(userId, platform, promptId);
    }

    @Override
    public boolean deleteByIds(Long[] windowIds, LoginUser loginUser) {
        return baseMapper.delete(new QueryWrapper<AiWindow>().eq("user_id",loginUser.getUserId()).in("window_id",windowIds)) > 0;
    }
}
