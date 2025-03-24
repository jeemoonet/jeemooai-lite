package com.jeemoo.system.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiCompany;
import com.jeemoo.system.domain.AiPrompt;
import com.jeemoo.system.domain.bo.AiPromptBo;
import com.jeemoo.system.domain.vo.AiPromptVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import com.jeemoo.system.param.CompanyPromptResponse;
import com.jeemoo.system.param.PromptListResponse;
import com.jeemoo.system.param.PromptSearchParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 提示器Mapper接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface AiPromptMapper extends BaseMapperPlus<AiPromptMapper, AiPrompt, AiPromptVo> {

    @Update("update ai_prompt set use_num = use_num + #{num} where prompt_id = #{promptId}")
    int addUseNum(@Param("promptId") Long promptId,@Param("num") int num);

    Page<AiPromptVo> listPage(@Param("page") Page<Object> build,@Param("bo") AiPromptBo bo);

    int setStatus(@Param("promptId") Long promptId,@Param("status") Integer status);

    Page<CompanyPromptResponse> companyPromptPage(@Param("page") Page<Object> page,@Param("param") PromptSearchParam param);

    @Select("select * from ai_prompt where prompt_id = #{promptId}")
    AiPrompt selectByIdNoDeleted(@Param("promptId") Long promptId);

    List<JSONObject> options(@Param("bo") AiPromptBo bo);

    void recent(@Param("promptId") Long promptId,@Param("userId") Long userId);

    List<PromptListResponse> chatSelect(@Param("userId") Long userId, @Param("companyId") Long companyId);

    List<PromptListResponse> search(@Param("companyId") Long companyId, @Param("keywords") String keywords);

    Page<PromptListResponse> apiPage(@Param("page") Page<Object> page,
                                     @Param("param") PromptSearchParam param);
}
