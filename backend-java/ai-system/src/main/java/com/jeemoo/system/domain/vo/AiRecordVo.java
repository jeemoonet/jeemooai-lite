package com.jeemoo.system.domain.vo;

import cn.hutool.json.JSONObject;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeemoo.system.domain.AiRecordFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天记录视图对象 ai_record
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long messageId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long windowId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long userId;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    @JsonProperty("message")
    private String content;

    private String reasoningContent;

    /**
     * user=用户发送,assistant=机器人回复	
     */
    @ExcelProperty(value = "user=用户发送,assistant=机器人回复	")
    private String role;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Integer isInitMessage;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Integer isLike;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long createTimestamp;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String masterUuid;

    private String type;

    /**
     * 
     */
    @JsonIgnore
    private String docInfo;

    @JsonProperty("docInfo")
    private List<JSONObject> docInfoList;

    @JsonIgnore
    private String searchInfo;

    private List<JSONObject> searchInfoList;

    private List<AiRecordFile> fileInfo;

    private Long promptId;

    private Long nextId;

    private Long memberId;

    private String memberNickname;

    private String avatar;

    private Integer isError;

    /**
     * 
     */
    @JsonIgnore
    private Integer deleted;

    public void addRecordFile(AiRecordFile recordFile) {
        if (this.fileInfo == null) {
            this.fileInfo = new ArrayList<>();
        }

        this.fileInfo.add(recordFile);
    }
}
