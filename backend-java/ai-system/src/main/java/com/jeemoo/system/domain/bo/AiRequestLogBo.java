package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;
import java.util.List;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 请求记录业务对象 ai_request_log
 *
 * @author ai
 * @date 2023-09-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiRequestLogBo extends BaseEntity {

    /**
     *
     */
    @NotBlank(message = "不能为空", groups = { EditGroup.class })
    private String uuid;

    private Long companyId;
    private String companyName;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;
    private String userName;
    private String nickName;

    /**
     *
     */
    @NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String key;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer wordCount;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer requestTime;

    /**
     *
     */
    @NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String model;

    /**
     *
     */
    @NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String errorMessage;
    private Integer status;

    /**
     * 发送给gpt的数据
     */
    @NotBlank(message = "发送给gpt的数据不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sendMessage;

    /**
     * gpt返回的数据
     */
    @NotBlank(message = "gpt返回的数据不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiveMessage;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long promptId;

    private String promptName;

    private String memberNickname;

    private String ip;

    private String userAgent;

    private String question;

    private Integer integral;

    private String functionName;

    private List<String> rangeTime;
}
