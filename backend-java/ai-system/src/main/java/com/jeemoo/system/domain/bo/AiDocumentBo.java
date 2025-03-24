package com.jeemoo.system.domain.bo;

import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.jeemoo.common.core.domain.BaseEntity;

/**
 * 文档业务对象 ai_document
 *
 * @author ai
 * @date 2023-09-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AiDocumentBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long documentId;

    /**
     * 文档类型：pdf,txt,word,url,wechat
     */
    @NotBlank(message = "文档类型：pdf,txt,word,url,wechat	不能为空", groups = { AddGroup.class, EditGroup.class })
    private String documentType;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String documentName;

    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空", groups = { AddGroup.class, EditGroup.class })
    private String documentDesc;

    /**
     * 字节数
     */
    @NotNull(message = "字节数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long fileSize;

    /**
     * 状态：0=未处理；1=进行中；2=已完成；3=失败
     */
    @NotNull(message = "状态：0=未处理；1=进行中；2=已完成；3=失败不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 页数
     */
    @NotNull(message = "页数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer pageNumber;

    /**
     * 备注，url抓取记录，错误记录
     */
    @NotBlank(message = "备注，url抓取记录，错误记录不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deleted;

    private String url;

    private Long companyId;

    private Long categoryId;

    private Long userId;

    private String companyName;

    private String keywords;

    private String splitName;

    private Integer length;

    private String splitChar;

    private Integer splitId;
}
