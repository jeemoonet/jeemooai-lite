package com.jeemoo.api.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.vo.AiPromptCategoryVo;
import com.jeemoo.system.param.AiModels;
import com.jeemoo.system.param.PromptListResponse;
import com.jeemoo.system.param.PromptSearchParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HomeMapper {
    List<PromptListResponse> recommendList(@Param("loginUser") LoginUser loginUser);

    @Select("select * from ai_prompt_category where company_id = #{loginUser.companyId} and deleted = 0 order by sort_num")
    List<AiPromptCategoryVo> categoryList(@Param("loginUser") LoginUser loginUser);

    Page<PromptListResponse> promptPage(@Param("page") Page<Object> page, @Param("param") PromptSearchParam param, @Param("userId") Long userId);

    List<PromptListResponse> recent(@Param("userId") Long userId,@Param("companyId") Long companyId);

    @Select("select model_name,id as model_label, icon, platform_name from ai_models")
    List<AiModels> models();

    List<JSONObject> dayIntegral(@Param("companyId") Long companyId,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime);

    List<JSONObject> appDayIntegral(@Param("companyId") Long companyId,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime);

    List<JSONObject> promptTop(@Param("companyId") Long companyId,
                               @Param("startTime") String startTime,
                               @Param("endTime") String endTime);

    List<JSONObject> userTop(@Param("companyId") Long companyId,
                             @Param("startTime") String startTime,
                             @Param("endTime") String endTime);

    List<JSONObject> repositoryRecommend();

    List<JSONObject> repositoryAll();

    List<JSONObject> promptWindowCount(@Param("companyId") Long companyId,
                                       @Param("promptId") Long promptId,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime);

    @Select("select prompt_id as promptId,prompt_name as promptName from ai_prompt where deleted = 0 and company_id = #{companyId} order by create_time desc")
    List<JSONObject> promptSelect(@Param("companyId") Long companyId);

    List<JSONObject> promptQuestionCount(@Param("companyId") Long companyId,
                                         @Param("promptId") Long promptId,
                                         @Param("startTime") String startTime,
                                         @Param("endTime") String endTime);

    List<JSONObject> promptGradeCount(@Param("companyId") Long companyId,
                                      @Param("promptId") Long promptId,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime);

    @Select("select * from ai_prompt_category where category_id in (select category_id from ai_prompt where deleted = 0 and is_public = 1 and status = 1 and company_id = #{loginUser.companyId}) and deleted = 0 order by sort_num")
    List<AiPromptCategoryVo> miniappCategoryList(@Param("loginUser") LoginUser loginUser);
}
