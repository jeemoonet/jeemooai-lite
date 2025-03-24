package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.ApiEditGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 企业资料业务对象 ai_company
 *
 * @author ai
 * @date 2023-09-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiCompanyBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long companyId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 个人手机
     */
    @NotBlank(message = "个人手机不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    @Length(min = 0,max = 255,message = "个人手机最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String personMobile;

    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "公司名称最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String companyName;

    /**
     * 行业类型
     */
//    @NotBlank(message = "行业类型不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "行业类型最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String industryType;
    private Long industryId;

    /**
     * 员工人数
     */
//    @NotBlank(message = "员工人数不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "员工人数最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String employeeNum;

    /**
     * 职位
     */
//    @NotBlank(message = "职位不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "职位最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String positionName;

    /**
     * 公司地址
     */
//    @NotBlank(message = "公司地址不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "公司地址最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String companyAddress;
    private String companyAddressCode;

    /**
     * 营业执照代码
     */
//    @NotBlank(message = "营业执照代码不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
//    @Length(min = 0,max = 255,message = "营业执照代码最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String licenseCode;

    /**
     * 营业执照
     */
//    @NotBlank(message = "营业执照不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
//    @Length(min = 0,max = 255,message = "营业执照最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String businessLicense;

    /**
     * 组织机构代码证书
     */
//    @NotBlank(message = "组织机构代码证书不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
//    @Length(min = 0,max = 255,message = "组织机构代码证书最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String organizationCodeCertificate;

    /**
     * 公司标志
     */
//    @NotBlank(message = "公司标志不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "公司标志最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String logo;

    /**
     * 公司描述
     */
//    @NotBlank(message = "公司描述不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "公司描述最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
    private String companyDesc;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空", groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class })
    @Length(min = 0,max = 255,message = "联系人姓名最长不能超过255个字符",groups = { AddGroup.class, EditGroup.class, ApiEditGroup.class})
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

    private String userName;
    private String nickName;
    private String remark;
}
