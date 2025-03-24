package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.entity.SysDictData;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.common.core.domain.ModelsConfig;
import com.jeemoo.system.mapper.SysDictDataMapper;
import com.jeemoo.system.param.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiModelsBo;
import com.jeemoo.system.domain.vo.AiModelsVo;
import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.mapper.AiModelsMapper;
import com.jeemoo.system.service.IAiModelsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 模型Service业务层处理
 *
 * @author ai
 * @date 2023-11-15
 */
@RequiredArgsConstructor
@Service
public class AiModelsServiceImpl implements IAiModelsService {

    private final AiModelsMapper baseMapper;
    private final SysDictDataMapper sysDictDataMapper;

    /**
     * 查询模型
     */
    @Override
    public AiModelsVo queryById(Integer id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询模型列表
     */
    @Override
    public TableDataInfo<AiModelsVo> queryPageList(AiModelsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiModels> lqw = buildQueryWrapper(bo);
        Page<AiModelsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<AiModelsVo> records = result.getRecords();
        List<SysDictData> platformNames = sysDictDataMapper.selectDictDataByType("model_platform_name");
        HashMap<String, String> map = new HashMap<>();
        for (SysDictData platformName : platformNames) {
            map.put(platformName.getDictValue(), platformName.getRemark());
        }
        for (AiModelsVo record : records) {
            if (record.getPlatformName() != null) {
                record.setIcon(map.get(record.getPlatformName()));
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询模型列表
     */
    @Override
    public List<AiModelsVo> queryList(AiModelsBo bo) {
        LambdaQueryWrapper<AiModels> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiModels> buildQueryWrapper(AiModelsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiModels> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getModelName()), AiModels::getModelName, bo.getModelName());
        lqw.eq(StringUtils.isNotBlank(bo.getModelLabel()), AiModels::getModelLabel, bo.getModelLabel());
        lqw.eq(bo.getPlatformName() != null, AiModels::getPlatformName, bo.getPlatformName());
        lqw.orderByDesc(AiModels::getId);
        return lqw;
    }

    /**
     * 新增模型
     */
    @Override
    public Boolean insertByBo(AiModelsBo bo) {
        AiModels add = BeanUtil.toBean(bo, AiModels.class);
        add.setPlatform("oneapi");
        add.setPlatformName("积木大脑");
        AiModels isDefault = baseMapper.selectOne(new QueryWrapper<AiModels>().eq("is_default", 1).last("limit 1"));
        if (isDefault == null) {
            add.setIsDefault(1);
        }
        AiModels isReasoningDefault = baseMapper.selectOne(new QueryWrapper<AiModels>().eq("is_reasoning_default", 1).last("limit 1"));
        if (isReasoningDefault == null) {
            add.setIsReasoningDefault(1);
        }
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改模型
     */
    @Override
    public Boolean updateByBo(AiModelsBo bo) {
        AiModels update = BeanUtil.toBean(bo, AiModels.class);
        update.setPlatformName("积木大脑");
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiModels entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除模型
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<AiModels> list() {
        return baseMapper.selectList();
    }

    @Override
    public List<AiModels> list(QueryWrapper<AiModels> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public int setDefault(Integer modelId) {
        AiModels model = baseMapper.selectById(modelId);
        if (model != null) {
            baseMapper.cleanDefault();
            return baseMapper.setDefault(modelId);
        } else {
            return 0;
        }
    }

    @Override
    public HashMap<String, ModelsConfig> getConfigs() {
        List<ModelsConfig> list = baseMapper.getConfigs();
        HashMap<String, ModelsConfig> hashMap = new HashMap<>();
        for (ModelsConfig modelsConfig : list) {
            hashMap.put(modelsConfig.getPlatform(), modelsConfig);
        }

        return hashMap;
    }

    @Override
    public ModelsConfig getConfig(String platform) {
        List<ModelsConfig> list = baseMapper.getConfigs();
        for (ModelsConfig item : list) {
            if (Objects.equals(platform, item.getPlatform())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void insert(List<AiModels> insertBatch) {
        baseMapper.insertBatch(insertBatch);
    }


    @Override
    public int setReasoningDefault(Integer modelId) {
        AiModels model = baseMapper.selectById(modelId);
        if (model != null) {
            baseMapper.cleanReasoningDefault();
            return baseMapper.setReasoningDefault(modelId);
        } else {
            return 0;
        }
    }

    @Override
    public boolean setModelConfig(InitSetting modelConfig) {
        int modelConfigResult = baseMapper.setModelConfig(modelConfig.getModelConfig());
        int embeddingConfigResult = baseMapper.setEmbeddingConfig(modelConfig.getEmbeddingConfig());
        int bingConfigResult = baseMapper.setBingConfig(modelConfig.getBingConfig());

        return modelConfigResult * embeddingConfigResult * bingConfigResult > 0;
    }

    @Override
    public InitSetting getModelConfig() {
        ModelInitConfig modelInitConfig = baseMapper.getModelConfig();
        EmbeddingInitConfig embeddingInitConfig = baseMapper.getEmbeddingConfig();
        BingInitConfig bingInitConfig = baseMapper.getBingConfig();

        InitSetting config = new InitSetting();
        if (modelInitConfig == null) {
            modelInitConfig = new ModelInitConfig();
            modelInitConfig.setKey("");
            modelInitConfig.setUrl("");
        }
        config.setModelConfig(modelInitConfig);
        if (embeddingInitConfig == null) {
            embeddingInitConfig = new EmbeddingInitConfig();
            embeddingInitConfig.setKey("");
            embeddingInitConfig.setSecret("");
        }
        config.setEmbeddingConfig(embeddingInitConfig);
        if (bingInitConfig == null) {
            bingInitConfig = new BingInitConfig();
            bingInitConfig.setSecret("");
            bingInitConfig.setEndpoint("");
        }
        config.setBingConfig(bingInitConfig);

        return config;
    }

    @Override
    public InitResult isInit() {
        String errorMsg = "";
        InitSetting initConfig = this.getModelConfig();
        ModelInitConfig modelConfig = initConfig.getModelConfig();
        if (modelConfig == null) {
            errorMsg = "请填写大模型配置";
        } else {
            if (StringUtils.isEmpty(modelConfig.getKey())) {
                errorMsg = "请填写大模型配置：key";
            }
            if (StringUtils.isEmpty(modelConfig.getUrl())) {
                errorMsg = "请填写大模型配置：url";
            }
        }
        EmbeddingInitConfig embeddingInitConfig = initConfig.getEmbeddingConfig();
        if (embeddingInitConfig == null) {
            errorMsg = "请填写向量模型配置";
        } else {
            if (StringUtils.isEmpty(embeddingInitConfig.getKey())) {
                errorMsg = "请填写向量模型配置：key";
            }
            if (StringUtils.isEmpty(embeddingInitConfig.getSecret())) {
                errorMsg = "请填写向量模型配置：secret";
            }
        }
        BingInitConfig bingInitConfig = initConfig.getBingConfig();
        if (bingInitConfig == null) {
            errorMsg = "请填写bing搜索配置";
        } else {
            if (StringUtils.isEmpty(bingInitConfig.getSecret())) {
                errorMsg = "请填写bing搜索配置：secret";
            }
            if (StringUtils.isEmpty(bingInitConfig.getEndpoint())) {
                errorMsg = "请填写bing搜索配置：endpoint";
            }
        }

        List<AiModels> models = this.list();
        if (models.isEmpty()) {
            errorMsg = "请添加大模型";
        }

        InitResult result = new InitResult();
        result.setErrorMsg(errorMsg);
        result.setIsInit(errorMsg.isEmpty() ? 1 : 0);
        return result;
    }
}
