package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 企业资料视图对象 ai_company
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiCompanyVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long companyId;

    /**
     * 用户ID
     */
//    @ExcelProperty(value = "用户ID")
    @JsonIgnore
    private Long userId;
    /**
     * 公司名称
     */
    @ExcelProperty(value = "公司名称")
    private String companyName;

    @ExcelProperty(value = "注册姓名")
    private String nickName;
    @ExcelProperty(value = "注册手机")
    private String userName;
    @ExcelProperty(value = "用户使用情况：已使用")
    private Integer userCount;
    @ExcelProperty(value = "用户使用情况：最大限制")
    private Integer userCountMax;
    @ExcelProperty(value = "积分使用情况：剩余积分")
    private Integer integral;
    @ExcelProperty(value = "积分使用情况：已使用")
    private Integer integralTotal;
    @ExcelProperty(value = "知识库容量：已使用")
    private Long documentWordCount;
    @ExcelProperty(value = "知识库容量：最大容量")
    private Long documentWordCountMax;
    private Integer isTrial;
    @ExcelProperty(value = "套餐")
    private String advancedPackage;
    @ExcelProperty(value = "有效期")
    private Date advancedEndTime;
    private Long advancedPackageId;
    private Integer advancedPackageLevel;
    @ExcelProperty(value = "渠道SN")
    private String channelSn;
    @ExcelProperty(value = "渠道姓名")
    private String channelRealName;
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 个人手机
     */
    @ExcelProperty(value = "个人手机")
    private String personMobile;

    /**
     * 行业类型
     */
    @ExcelProperty(value = "行业类型")
    private String industryType;
    private Long industryId;

    /**
     * 员工人数
     */
    @ExcelProperty(value = "员工人数")
    private String employeeNum;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位")
    private String positionName;

    /**
     * 公司地址
     */
    @ExcelProperty(value = "公司地址")
    private String companyAddress;
    private String companyAddressCode;

    /**
     * 营业执照代码
     */
    @ExcelProperty(value = "营业执照代码")
    private String licenseCode;

    /**
     * 营业执照
     */
    @ExcelProperty(value = "营业执照")
    private String businessLicense;

    /**
     * 组织机构代码证书
     */
    @ExcelProperty(value = "组织机构代码证书")
    private String organizationCodeCertificate;

    /**
     * 公司标志
     */
//    @ExcelProperty(value = "公司标志")
    private String logo;

    /**
     * 公司描述
     */
    @ExcelProperty(value = "公司描述")
    private String companyDesc;

    /**
     * 联系人姓名
     */
    @ExcelProperty(value = "联系人姓名")
    private String personName;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 删除标记
     */
//    @ExcelProperty(value = "删除标记")
    @JsonIgnore
    private Integer deleted;
}
