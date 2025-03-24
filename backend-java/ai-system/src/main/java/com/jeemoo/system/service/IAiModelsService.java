package com.jeemoo.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.domain.vo.AiModelsVo;
import com.jeemoo.system.domain.bo.AiModelsBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.ModelsConfig;
import com.jeemoo.system.param.InitResult;
import com.jeemoo.system.param.InitSetting;
import com.jeemoo.system.param.ModelInitConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 模型Service接口
 *
 * @author ai
 * @date 2023-11-15
 */
public interface IAiModelsService {

    /**
     * 查询模型
     */
    AiModelsVo queryById(Integer id);

    /**
     * 查询模型列表
     */
    TableDataInfo<AiModelsVo> queryPageList(AiModelsBo bo, PageQuery pageQuery);

    /**
     * 查询模型列表
     */
    List<AiModelsVo> queryList(AiModelsBo bo);

    /**
     * 新增模型
     */
    Boolean insertByBo(AiModelsBo bo);

    /**
     * 修改模型
     */
    Boolean updateByBo(AiModelsBo bo);

    /**
     * 校验并批量删除模型信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    List<AiModels> list();
    List<AiModels> list(QueryWrapper<AiModels> queryWrapper);

    int setDefault(Integer modelId);

    HashMap<String, ModelsConfig> getConfigs();

    ModelsConfig getConfig(String platform);

    void insert(List<AiModels> insertBatch);

    int setReasoningDefault(Integer modelId);

    boolean setModelConfig(InitSetting modelConfig);

    InitSetting getModelConfig();

    InitResult isInit();
}
