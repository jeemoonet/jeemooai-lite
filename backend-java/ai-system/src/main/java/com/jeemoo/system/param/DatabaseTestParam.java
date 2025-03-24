package com.jeemoo.system.param;

import com.jeemoo.common.core.domain.BaseEntity;
import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 数据库业务对象 ai_databases
 *
 * @author ai
 * @date 2024-09-09
 */

@Data
public class DatabaseTestParam {

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String database_name;

    /**
     * 类型database_name
     */
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 端口
     */
    @NotBlank(message = "端口不能为空")
    private String port;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;


}
