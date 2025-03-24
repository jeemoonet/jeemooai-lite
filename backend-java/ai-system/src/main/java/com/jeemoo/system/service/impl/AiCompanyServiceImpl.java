package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.exception.GlobalException;
import com.jeemoo.common.exception.ServiceException;
import com.jeemoo.common.exception.biz.IntegralBalanceException;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.domain.*;
import com.jeemoo.system.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiCompanyBo;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.service.IAiCompanyService;

import java.util.*;

/**
 * 企业资料Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiCompanyServiceImpl implements IAiCompanyService {

    private final AiCompanyMapper baseMapper;
    private final AiModelsMapper modelsMapper;
    /**
     * 查询企业资料
     */
    @Override
    public AiCompanyVo queryById(Long companyId){
        return baseMapper.selectVoById(companyId);
    }

    /**
     * 查询企业资料列表
     */
    @Override
    public TableDataInfo<AiCompanyVo> queryPageList(AiCompanyBo bo, PageQuery pageQuery) {
        QueryWrapper<AiCompany> lqw = buildQueryWrapper(bo);
        Page<AiCompanyVo> result = baseMapper.page(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询企业资料列表
     */
    @Override
    public List<AiCompanyVo> queryList(AiCompanyBo bo) {
        QueryWrapper<AiCompany> lqw = buildQueryWrapper(bo);
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(100000);
        Page<AiCompanyVo> result = baseMapper.page(pageQuery.build(), lqw);
        return result.getRecords();
    }

    private QueryWrapper<AiCompany> buildQueryWrapper(AiCompanyBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<AiCompany> lqw = Wrappers.query();
        lqw.eq(bo.getUserId() != null, "a.user_id", bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getPersonMobile()), "a.person_mobile", bo.getPersonMobile());
        lqw.like(StringUtils.isNotBlank(bo.getCompanyName()), "a.company_name", bo.getCompanyName());
        lqw.eq(StringUtils.isNotBlank(bo.getIndustryType()), "a.industry_type", bo.getIndustryType());
        lqw.eq(StringUtils.isNotBlank(bo.getEmployeeNum()), "a.employee_num", bo.getEmployeeNum());
        lqw.like(StringUtils.isNotBlank(bo.getPositionName()), "a.position_name", bo.getPositionName());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanyAddress()), "a.company_address", bo.getCompanyAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getLicenseCode()), "a.license_code", bo.getLicenseCode());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessLicense()), "a.businessLicense", bo.getBusinessLicense());
        lqw.eq(StringUtils.isNotBlank(bo.getOrganizationCodeCertificate()), "a.organization_code_certificate", bo.getOrganizationCodeCertificate());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanyDesc()), "a.company_desc", bo.getCompanyDesc());
        lqw.like(StringUtils.isNotBlank(bo.getPersonName()), "a.person_name", bo.getPersonName());
        lqw.eq(StringUtils.isNotBlank(bo.getChannelSn()), "a.channel_sn", bo.getChannelSn());
        lqw.eq(StringUtils.isNotBlank(bo.getUserName()), "c.user_name",bo.getUserName());
        lqw.between(ObjectUtil.isNotNull(params.get("endTimeStart")),"a.advanced_end_time",params.get("endTimeStart"),params.get("endTimeEnd"));
        return lqw;
    }

    /**
     * 新增企业资料
     */
    @Override
    public Boolean insertByBo(AiCompanyBo bo) {
        AiCompany add = BeanUtil.toBean(bo, AiCompany.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setCompanyId(add.getCompanyId());
        }
        return flag;
    }

    /**
     * 修改企业资料
     */
    @Override
    public Boolean updateByBo(AiCompanyBo bo) {
        AiCompany update = BeanUtil.toBean(bo, AiCompany.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiCompany entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除企业资料
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public void updateUserCompanyName(Long userId, String companyName) {
        baseMapper.updateUserCompanyName(userId,companyName);
    }

    @Override
    public int updateRemark(AiCompanyBo bo) {
        return baseMapper.updateRemark(bo.getCompanyId(),bo.getRemark());
    }

    @Override
    public int isManager(LoginUser user) {
        AiCompany company = baseMapper.selectById(user.getCompanyId());
        return Objects.equals(user.getUserId(), company.getUserId()) ? 1 : 0;
    }
}
