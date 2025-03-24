package com.jeemoo.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeemoo.system.domain.AiCompany;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import com.jeemoo.system.domain.vo.AiDocumentVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 企业资料Mapper接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface AiCompanyMapper extends BaseMapperPlus<AiCompanyMapper, AiCompany, AiCompanyVo> {

    int subIntegral(@Param("companyId") Long companyId,@Param("integral") Integer integral);

    @Update("update ai_company set user_count = user_count + 1 where company_id = #{companyId}")
    void addUserCount(@Param("companyId") Long companyId);

    void subUsersCount(@Param("userIds") Long[] userIds);

    @Update("update ai_company set document_word_count = document_word_count + #{wordCount} where company_id = #{companyId}")
    void addDocumentWordCount(@Param("companyId") Long companyId,@Param("wordCount") Long wordCount);

    void subDowcumentWordCount(@Param("document") AiDocumentVo document);

    @Update("update sys_user set company_name = #{companyName} where user_id = #{userId}")
    int updateUserCompanyName(@Param("userId") Long userId,@Param("companyName") String companyName);

    Page<AiCompanyVo> page(@Param("page") Page<Object> page,@Param(Constants.WRAPPER) QueryWrapper<AiCompany> wrapper);

    int countCompanySharedPrompt(@Param("companyId") Long companyId,@Param("promptId") Long promptId);

    @Update("update ai_company set remark = #{remark} where company_id = #{companyId}")
    int updateRemark(@Param("companyId") Long companyId,@Param("remark") String remark);
}
