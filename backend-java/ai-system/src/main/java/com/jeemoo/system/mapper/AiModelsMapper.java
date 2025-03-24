package com.jeemoo.system.mapper;

import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.domain.vo.AiModelsVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import com.jeemoo.common.core.domain.ModelsConfig;
import com.jeemoo.system.param.BingInitConfig;
import com.jeemoo.system.param.EmbeddingInitConfig;
import com.jeemoo.system.param.ModelInitConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 模型Mapper接口
 *
 * @author ai
 * @date 2023-11-15
 */
public interface AiModelsMapper extends BaseMapperPlus<AiModelsMapper, AiModels, AiModelsVo> {

    @Update("update ai_models set status = #{status} where id = #{id}")
    int changeStatus(@Param("id") Integer id,@Param("status") Integer status);

    @Update("update ai_models set is_default = 0")
    int cleanDefault();

    @Update("update ai_models set is_default = 1 where id = #{modelId}")
    int setDefault(@Param("modelId") Integer modelId);

    @Select("select * from ai_models_config")
    List<ModelsConfig> getConfigs();

    @Update("update ai_models set response_json = #{responseJson} where id = #{id}")
    int changeResponseJson(@Param("id") Integer id,@Param("responseJson") Integer responseJson);

    @Update("update ai_models set is_reasoning_default = 0")
    void cleanReasoningDefault();

    @Update("update ai_models set is_reasoning_default = 1 where id = #{modelId}")
    int setReasoningDefault(Integer modelId);

    @Update("update ai_models_config set `key` = #{modelConfig.key}, `url` = #{modelConfig.url} where platform = 'oneapi'")
    int setModelConfig(@Param("modelConfig") ModelInitConfig modelConfig);

    @Update("update ai_models_config set `key` = #{embeddingConfig.key}, `secret` = #{embeddingConfig.secret} where platform = 'qian_fan'")
    int setEmbeddingConfig(@Param("embeddingConfig") EmbeddingInitConfig embeddingConfig);

    @Update("update ai_models_config set `secret` = #{bingConfig.secret}, `url` = #{bingConfig.endpoint} where platform = 'bing'")
    int setBingConfig(@Param("bingConfig") BingInitConfig bingConfig);

    @Select("select `key`, `url` from ai_models_config where platform = 'oneapi'")
    ModelInitConfig getModelConfig();

    @Select("select `key`, `secret` from ai_models_config where platform = 'qian_fan'")
    EmbeddingInitConfig getEmbeddingConfig();

    @Select("select `url` as endpoint, `secret` from ai_models_config where platform = 'bing'")
    BingInitConfig getBingConfig();
}
