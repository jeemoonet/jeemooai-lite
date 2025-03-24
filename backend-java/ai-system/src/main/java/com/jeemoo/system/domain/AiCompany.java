package com.jeemoo.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 企业资料对象 ai_company
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_company")
public class AiCompany extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "company_id")
    private Long companyId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 个人手机
     */
    private String personMobile;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 行业类型
     */
    private String industryType;
    private Long industryId;
    /**
     * 员工人数
     */
    private String employeeNum;
    /**
     * 职位
     */
    private String positionName;
    /**
     * 公司地址
     */
    private String companyAddress;
    private String companyAddressCode;
    /**
     * 营业执照代码
     */
    private String licenseCode;
    /**
     * 营业执照
     */
    private String businessLicense;
    /**
     * 组织机构代码证书
     */
    private String organizationCodeCertificate;
    /**
     * 公司标志
     */
    private String logo;
    /**
     * 公司描述
     */
    private String companyDesc;
    /**
     * 联系人姓名
     */
    private String personName;

    private Integer userCount;
    private Integer userCountMax;
    private Integer integral;
    private Integer integralTotal;
    private Long documentWordCount;
    private Long documentWordCountMax;
    private Integer isTrial;
    private String advancedPackage;
    private Date advancedEndTime;
    private Long advancedPackageId;
    private Integer advancedPackageLevel;
    private String channelSn;
    private String remark;

    /**
     * 删除标记
     */
    @TableLogic
    private Integer deleted;

}
