package com.jeemoo.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiWindow;
import com.jeemoo.system.domain.vo.AiWindowVo;
import com.jeemoo.system.domain.bo.AiWindowBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.system.param.*;

import java.util.Collection;
import java.util.List;

/**
 * 窗口Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiWindowService {

    /**
     * 查询窗口
     */
    AiWindowVo queryById(Long windowId);

    /**
     * 查询窗口列表
     */
    TableDataInfo<AiWindowVo> queryPageList(AiWindowBo bo, PageQuery pageQuery);

    /**
     * 查询窗口列表
     */
    List<AiWindowVo> queryList(AiWindowBo bo);

    /**
     * 新增窗口
     */
    Boolean insertByBo(AiWindowBo bo);

    /**
     * 修改窗口
     */
    Boolean updateByBo(AiWindowBo bo);

    /**
     * 校验并批量删除窗口信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    AiWindow fetchWindow(SendMessageParam param, LoginUser loginUser, String platform);

    List<WindowListResponse> listByUser(LoginUser loginUser, int talkType, String platform, Long filterWindowId);

    boolean deleteById(Long windowId);

    boolean rename(RenameWindowParam param, LoginUser loginUser);

    AiWindow findOne(Long windowId);

    AiWindow getOne(QueryWrapper<AiWindow> queryWrapper);

    void insert(AiWindow window);

    TableDataInfo<CompanyWindowResponse> pageByCompany(LoginUser loginUser, CompanyWindowParam param, PageQuery pageQuery);

    List<AiWindow> fetchTestWindow(SendTestMessageParam param, LoginUser loginUser, String uuid);

    Long findOneFreeWindow(Long userId);
    Long findOneFreeWindow(Long userId, String platform);
    Long findOneFreeWindow(Long userId, String platform, Long promptId);

    boolean deleteByIds(Long[] windowIds, LoginUser loginUser);
}
