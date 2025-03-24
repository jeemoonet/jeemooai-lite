package com.jeemoo.system.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegisterParam {
    /**
     * 手机号
     */
    @NotEmpty(message = "联系人不能为空")
    @Length(max = 255,message = "联系人长度不能超过255个字符")
    private String nickName;

    /**
     * 联系人姓名
     */
    @NotEmpty(message = "手机号不能为空")
    @Length(max = 255,message = "手机号长度不能超过255个字符")
    private String userName;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 公司名称
     */
    @NotEmpty(message = "公司名称不能为空")
    @Length(max = 255,message = "公司名称不能超过255个字符")
    private String companyName;

    private String code;

    private String uuid;

    @Length(max = 100,message = "密码长度不能超过100个字符")
    private String password;

    @NotNull(message = "行业不能为空")
    private Long industryId;

    private String channelSn;

    private Long channelShareId;

    private String loginCode;

    private String appid;
}
