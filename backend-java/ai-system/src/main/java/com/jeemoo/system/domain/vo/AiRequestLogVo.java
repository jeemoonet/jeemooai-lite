package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jeemoo.common.annotation.ExcelDictFormat;
import com.jeemoo.common.annotation.Sensitive;
import com.jeemoo.common.convert.ExcelDictConvert;
import com.jeemoo.common.enums.SensitiveStrategy;
import lombok.Data;
import java.util.Date;



/**
 * 请求记录视图对象 ai_request_log
 *
 * @author ai
 * @date 2023-09-12
 */
@Data
@ExcelIgnoreUnannotated
public class AiRequestLogVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "UUID")
    private String uuid;

    @ExcelProperty(value = "MemberId")
    private Long memberId;
    @ExcelProperty(value = "外部用户")
    private String memberNickname;
    /**
     *
     */
    @ExcelProperty(value = "UserId")
    private Long userId;
    @ExcelProperty(value = "内部用户")
    private String nickName;
    private String userName;

    /**
     *
     */
//    @ExcelProperty(value = "KEY")
    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    private String key;

    /**
     *
     */
    @ExcelProperty(value = "字数")
    private Integer wordCount;

    /**
     *
     */
    @ExcelProperty(value = "请求时间")
    private Integer requestTime;

    /**
     *
     */
    @ExcelProperty(value = "Model")
    private String model;

    /**
     *
     */
    @ExcelProperty(value = "错误信息")
    private String errorMessage;

    /**
     * 发送给gpt的数据
     */
//    @ExcelProperty(value = "发送给gpt的数据")
    private String sendMessage;

    /**
     * gpt返回的数据
     */
//    @ExcelProperty(value = "gpt返回的数据")
    private String receiveMessage;

    /**
     *
     */
    @ExcelProperty(value = "提示器ID")
    private Long promptId;
    @ExcelProperty(value = "提示器名称")
    private String promptName;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    private String ip;

    private String userAgent;

    private String question;

    @ExcelProperty(value = "消耗积分")
    private Integer integral;

    @ExcelProperty(value = "Function Name")
    private String functionName;

    @ExcelProperty(value = "企业名称")
    private String companyName;
}
