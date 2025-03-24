package com.jeemoo.system.service;

import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiPrompt;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.domain.bo.AiCompanyBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 企业资料Service接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface IAiCompanyService {

    /**
     * 查询企业资料
     */
    AiCompanyVo queryById(Long companyId);

    /**
     * 查询企业资料列表
     */
    TableDataInfo<AiCompanyVo> queryPageList(AiCompanyBo bo, PageQuery pageQuery);

    /**
     * 查询企业资料列表
     */
    List<AiCompanyVo> queryList(AiCompanyBo bo);

    /**
     * 新增企业资料
     */
    Boolean insertByBo(AiCompanyBo bo);

    /**
     * 修改企业资料
     */
    Boolean updateByBo(AiCompanyBo bo);

    /**
     * 校验并批量删除企业资料信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    void updateUserCompanyName(Long userId, String companyName);


    int updateRemark(AiCompanyBo bo);

    int isManager(LoginUser user);
}
