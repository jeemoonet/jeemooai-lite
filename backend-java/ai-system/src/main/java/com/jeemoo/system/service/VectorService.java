package com.jeemoo.system.service;

import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.system.param.VectorData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface VectorService {

    List<BigDecimal> embedding(String text);

    List<List<BigDecimal>> embedding(List<String> texts);

    List<VectorData> embeddingSearch(String text, List<Long> documentIds, String indexName, Integer topK, Float score) throws IOException;
    List<VectorData> longHistorySearch(String text, Long windowId, Long userId, Integer topK, Float score) throws IOException;
    List<VectorData> fullTextSearch(String text, List<Long> documentIds, String indexName, Integer topK) throws IOException;

    TableDataInfo<VectorData> listPage(String indexName, Long documentId, int pageNum, int pageSize, String keywords) throws IOException;

    boolean insertVector(String indexName, VectorData vectorData) throws IOException;

    VectorData getVector(String indexName, String id) throws IOException;

    boolean updateVector(String indexName, VectorData vectorData) throws IOException;

    List<VectorData> listByIds(String s, List<String> ids) throws IOException;

    boolean deleteVector(String indexName, List<String> ids) throws IOException;
}
