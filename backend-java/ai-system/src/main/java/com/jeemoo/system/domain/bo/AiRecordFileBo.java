package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 对话文件业务对象 ai_record_file
 *
 * @author ai
 * @date 2024-05-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiRecordFileBo extends BaseEntity {

    /**
     * 
     */
    @NotBlank(message = "不能为空", groups = { EditGroup.class })
    private String uuid;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileName;

    /**
     * icon
     */
    @NotBlank(message = "icon不能为空", groups = { AddGroup.class, EditGroup.class })
    private String icon;

    /**
     * 文件大小
     */
    @NotNull(message = "文件大小不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long fileSize;

    /**
     * 识别内容
     */
    @NotBlank(message = "识别内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 文件类型
     */
    @NotBlank(message = "文件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileType;

    /**
     * 文件url地址
     */
    @NotBlank(message = "文件url地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileUrl;

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long recordId;


}
