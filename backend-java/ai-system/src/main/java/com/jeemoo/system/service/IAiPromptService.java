package com.jeemoo.system.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiPrompt;
import com.jeemoo.system.domain.AiWindow;
import com.jeemoo.system.param.*;
import com.jeemoo.system.domain.vo.AiPromptVo;
import com.jeemoo.system.domain.bo.AiPromptBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 提示器Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiPromptService {

    /**
     * 查询提示器
     */
    AiPromptVo queryById(Long promptId);

    /**
     * 查询提示器列表
     */
    TableDataInfo<AiPromptVo> queryPageList(AiPromptBo bo, PageQuery pageQuery);

    /**
     * 查询提示器列表
     */
    List<JSONObject> options(AiPromptBo bo);

    /**
     * 新增提示器
     */
    Boolean insertByBo(AiPromptBo bo);

    /**
     * 修改提示器
     */
    Boolean updateByBo(AiPromptBo bo);

    /**
     * 校验并批量删除提示器信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean create(PromptCreateParam param) throws Exception;

    List<MenuTree> tree();

    boolean update(PromptCreateParam param) throws Exception;

    boolean deleteById(Long promptId);

    AiPromptVo info(Long promptId);

    AiPrompt fetchPrompt(AiWindow window, LoginUser loginUser);

    void addUseNum(Long promptId, int i);

    AiPrompt fineById(Long promptId);

    List<AiPrompt> list(QueryWrapper<AiPrompt> queryWrapper);

    void insertBatch(List<AiPrompt> promptSaveList);

    long count(QueryWrapper<AiPrompt> queryWrapper);

    boolean setStatus(Long promptId, Integer status);

    TableDataInfo<CompanyPromptResponse> companyPromptPage(PromptSearchParam param, PageQuery pageQuery);

    boolean saveBatch(List<AiPrompt> list);

    boolean insert(AiPrompt prompt);

    List<AiPromptVo> queryList(AiPromptBo bo);

    void recent(AiPrompt prompt, LoginUser loginUser);

    List<ChatPromptSelectResponse> chatSelect(LoginUser loginUser);

    List<PromptListResponse> search(String keywords);

    TableDataInfo<PromptListResponse> page(PageQuery page, PromptSearchParam param);

    boolean copy(PromptCreateParam param);

    boolean updateBaseInfo(PromptCreateParam param);

    boolean updateById(AiPrompt prompt);
}
