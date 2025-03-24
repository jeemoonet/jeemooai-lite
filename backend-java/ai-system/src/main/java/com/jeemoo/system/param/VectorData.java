package com.jeemoo.system.param;

import cn.hutool.json.JSONObject;
import com.jeemoo.common.utils.StringUtils;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VectorData {
    private String id;

    private String content;
    private Long categoryId;
    private Long documentId;
    private Long contentWordCount;
    private float score;

    public VectorData(String id, String content,Long contentWordCount, Long categoryId, Long documentId, float score) {
        this.id = id;
        this.content = content;
        this.categoryId = categoryId;
        this.documentId = documentId;
        this.contentWordCount = contentWordCount;
        this.score = score;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.set("content",this.content);
        json.set("content_word_count", contentWordCount);
        json.set("document_id",this.documentId);
        json.set("category_id",this.categoryId);
        if (StringUtils.isNotEmpty(this.id)) {
            json.set("_id", this.id);
        }
        return json;
    }

}
