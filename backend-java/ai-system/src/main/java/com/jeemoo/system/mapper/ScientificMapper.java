package com.jeemoo.system.mapper;

import cn.hutool.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ScientificMapper {

    @Select("select count(1) from ai_company where deleted = 0")
    int companyCount();

    @Select("select count(1) from sys_user where del_flag = 0")
    int userCount();

    @Select("select count(1) from ai_prompt where deleted = 0")
    int promptCount();


    @Select("select count(1) from ai_prompt_repository")
    int repoCount();

    List<JSONObject> companyDayCount(@Param("companyId") Long companyId,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime);

    List<JSONObject> userDayCount(@Param("companyId") Long companyId,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime);

    List<JSONObject> windowDayCount(@Param("companyId") Long companyId,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime);

    List<JSONObject> recordDayCount(@Param("companyId") Long companyId,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime);

    List<JSONObject> companyTop(@Param("companyId") Long companyId,
                                @Param("startTime") String startTime,
                                @Param("endTime") String endTime);

    List<JSONObject> promptTop(@Param("companyId") Long companyId,
                               @Param("startTime") String startTime,
                               @Param("endTime") String endTime);

    List<JSONObject> modelTop(@Param("companyId") Long companyId,
                              @Param("startTime") String startTime,
                              @Param("endTime") String endTime);
}
