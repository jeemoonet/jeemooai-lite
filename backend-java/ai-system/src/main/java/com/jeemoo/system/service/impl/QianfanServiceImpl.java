package com.jeemoo.system.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.model.embedding.EmbeddingData;
import com.baidubce.qianfan.model.embedding.EmbeddingResponse;
import com.jeemoo.common.core.domain.ModelsConfig;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.exception.ServiceException;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.system.param.VectorData;
import com.jeemoo.system.service.IAiModelsService;
import com.jeemoo.system.service.VectorService;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class QianfanServiceImpl implements VectorService {

    private Qianfan qianfan;

    @Autowired
    private IAiModelsService modelsService;
    @Autowired
    private RestClient esClient;

    @PostConstruct
    public void init() {
        ModelsConfig config = modelsService.getConfig("qian_fan");
        this.qianfan = new Qianfan(config.getKey(), config.getSecret());
    }

    @Override
    public List<BigDecimal> embedding(String text) {
        List<String> texts = new ArrayList<>();
        texts.add(text);
        List<List<BigDecimal>> vectors = this.embedding(texts);
        return vectors.get(0);
    }

    @Override
    public List<List<BigDecimal>> embedding(List<String> texts) {
        EmbeddingResponse resp = qianfan.embedding()
                .model("bge-large-zh")
                .input(texts)
                .execute();
        List<List<BigDecimal>> results = new ArrayList<>();
        for (EmbeddingData data : resp.getData()) {
            List<BigDecimal> embedding = data.getEmbedding();
            results.add(embedding);
        }
        return results;
    }

    @Override
    public List<VectorData> embeddingSearch(String text, List<Long> documentIds, String indexName, Integer topK, Float queryScore) throws IOException {
        if (topK == null || topK == 0) topK = 4;
        List<BigDecimal> embedding = this.embedding(text);
        Request request = new Request("GET", "/"+indexName+"/_search");
        JSONObject body = new JSONObject();
        JSONObject knn = new JSONObject();
        List<String> fields = new ArrayList<>();
        knn.set("field","content_vector");
        knn.set("query_vector", embedding);
        knn.set("k", topK);
        knn.set("num_candidates", 100);
        if (queryScore != null && queryScore != 0) {
            knn.set("similarity", queryScore);
        }
        JSONObject terms = new JSONObject();
        terms.set("document_id", documentIds);
        JSONObject filter = new JSONObject();
        filter.set("terms",terms);
        knn.set("filter", filter);
        fields.add("content");
        fields.add("document_id");
        fields.add("category_id");
        fields.add("content_word_count");
        body.set("knn",knn);
        body.set("fields",fields);
        body.set("_source", fields);
        request.setJsonEntity(body.toString());

        Response response = esClient.performRequest(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            List<JSONObject> hits = jsonObject.getJSONObject("hits").getBeanList("hits", JSONObject.class);
            List<VectorData> vectorDataList = new ArrayList<>();
            for (JSONObject hit : hits) {
                String id = hit.getStr("_id");
                Float score = hit.getFloat("_score");
                JSONObject _source = hit.getJSONObject("_source");
                Long documentId = _source.getLong("document_id");
                Long categoryId = _source.getLong("category_id");
                Long contentWordCount = _source.getLong("content_word_count");
                String content = _source.getStr("content");
                VectorData vector = new VectorData(id, content, contentWordCount,categoryId, documentId, score * 2 - 1);
                vectorDataList.add(vector);
            }
            return vectorDataList;
        } else {
            throw new ServiceException(EntityUtils.toString(response.getEntity()));
        }

    }

    @Override
    public List<VectorData> longHistorySearch(String text, Long windowId, Long userId, Integer topK, Float queryScore) throws IOException {
        if (topK == null || topK == 0) topK = 4;
        List<BigDecimal> embedding = this.embedding(text);
        Request request = new Request("GET", "/long_history_index/_search");
        JSONObject body = new JSONObject();
        JSONObject knn = new JSONObject();
        List<String> fields = new ArrayList<>();
        knn.set("field","content_vector");
        knn.set("query_vector", embedding);
        knn.set("k", topK);
        knn.set("num_candidates", 100);
        if (queryScore != null && queryScore != 0) {
            knn.set("similarity", queryScore);
        }
        JSONObject terms = new JSONObject();
        List<Long> windowIds = new ArrayList<>();
        windowIds.add(windowId);
        terms.set("window_id", windowIds);
        JSONObject filter = new JSONObject();
        filter.set("terms",terms);
        knn.set("filter", filter);
        fields.add("content");
        fields.add("window_id");
        fields.add("user_id");
        body.set("knn",knn);
        body.set("fields",fields);
        body.set("_source", fields);
        request.setJsonEntity(body.toString());

        Response response = esClient.performRequest(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            List<JSONObject> hits = jsonObject.getJSONObject("hits").getBeanList("hits", JSONObject.class);
            List<VectorData> vectorDataList = new ArrayList<>();
            for (JSONObject hit : hits) {
                String id = hit.getStr("_id");
                Float score = hit.getFloat("_score");
                JSONObject _source = hit.getJSONObject("_source");
                String content = _source.getStr("content");
                VectorData vector = new VectorData(id, content, null,null, null, score * 2 - 1);
                vectorDataList.add(vector);
            }
            return vectorDataList;
        } else {
            throw new ServiceException(EntityUtils.toString(response.getEntity()));
        }
    }

    @Override
    public List<VectorData> fullTextSearch(String text, List<Long> documentIds, String indexName, Integer topK) throws IOException {
        if (topK == null || topK == 0) topK = 4;
        Request request = new Request("GET", "/"+indexName+"/_search");
        JSONObject body = new JSONObject();
        JSONObject query = new JSONObject();
        JSONObject terms = new JSONObject();
        JSONObject termsDocumentIds = new JSONObject();
        JSONObject matchContent = new JSONObject();
        JSONObject match = new JSONObject();
        matchContent.set("content", text);
        match.set("match", matchContent);
        termsDocumentIds.set("document_id", documentIds);
        terms.set("terms", termsDocumentIds);
        List<JSONObject> must = new ArrayList<>();
        must.add(terms);
        must.add(match);
        JSONObject bool = new JSONObject();
        bool.set("must",must);
        query.set("bool", bool);
        body.set("query", query);
        body.set("size", topK);

        List<String> fields = new ArrayList<>();
        fields.add("content");
        fields.add("document_id");
        fields.add("category_id");
        fields.add("content_word_count");
        body.set("_source", fields);
        String bodyStr = body.toString();
        request.setJsonEntity(bodyStr);

        Response response = esClient.performRequest(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            List<JSONObject> hits = jsonObject.getJSONObject("hits").getBeanList("hits", JSONObject.class);
            List<VectorData> vectorDataList = new ArrayList<>();
            for (JSONObject hit : hits) {
                String id = hit.getStr("_id");
                Float score = hit.getFloat("_score");
                JSONObject _source = hit.getJSONObject("_source");
                Long documentId = _source.getLong("document_id");
                Long categoryId = _source.getLong("category_id");
                Long contentWordCount = _source.getLong("content_word_count");
                String content = _source.getStr("content");
                VectorData vector = new VectorData(id, content, contentWordCount,categoryId, documentId, score);
                vectorDataList.add(vector);
            }
            return vectorDataList;
        } else {
            throw new ServiceException(EntityUtils.toString(response.getEntity()));
        }
    }

    @Override
    public TableDataInfo<VectorData> listPage(String indexName, Long queeryDocumentId, int pageNum, int pageSize, String keywords) throws IOException {
        List<String> documentIds = new ArrayList<>();
        documentIds.add(String.valueOf(queeryDocumentId));
        Request request = new Request("GET", "/"+indexName+"/_search");
        JSONObject body = new JSONObject();
        JSONObject query = new JSONObject();
        JSONObject terms = new JSONObject();
        JSONObject termsDocumentIds = new JSONObject();
        JSONObject matchContent = new JSONObject();
        JSONObject match = new JSONObject();
        matchContent.set("content", keywords);
        match.set("match", matchContent);
        termsDocumentIds.set("document_id", documentIds);
        terms.set("terms", termsDocumentIds);
        List<JSONObject> must = new ArrayList<>();
        must.add(terms);
        if (StringUtils.isNotEmpty(keywords)) {
            must.add(match);
        }
        JSONObject bool = new JSONObject();
        bool.set("must",must);
        query.set("bool", bool);
        body.set("query", query);
        body.set("from", (pageNum - 1) * pageSize);
        body.set("size", pageSize);

        List<String> fields = new ArrayList<>();
        fields.add("content");
        fields.add("document_id");
        fields.add("category_id");
        body.set("_source", fields);
        String bodyStr = body.toString();
        request.setJsonEntity(bodyStr);

        Response response = esClient.performRequest(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            List<JSONObject> hits = jsonObject.getJSONObject("hits").getBeanList("hits", JSONObject.class);
            List<VectorData> vectorDataList = new ArrayList<>();
            for (JSONObject hit : hits) {
                String id = hit.getStr("_id");
                Float score = hit.getFloat("_score");
                JSONObject _source = hit.getJSONObject("_source");
                Long documentId = _source.getLong("document_id");
                Long categoryId = _source.getLong("category_id");
                Long contentWordCount = _source.getLong("content_word_count");
                String content = _source.getStr("content");
                VectorData vector = new VectorData(id, content, contentWordCount,categoryId, documentId, score);
                vectorDataList.add(vector);
            }
            TableDataInfo<VectorData> tableDataInfo = new TableDataInfo<>();
            tableDataInfo.setCode(200);
            tableDataInfo.setRows(vectorDataList);

            Request requestCount = new Request("GET", "/"+indexName+"/_count");
            JSONObject countBody = new JSONObject();
            countBody.set("query",query);
            requestCount.setJsonEntity(countBody.toString());
            Response responseCount = esClient.performRequest(requestCount);
            String responseCountBody = EntityUtils.toString(responseCount.getEntity());
            JSONObject countData = JSONUtil.parseObj(responseCountBody);
            tableDataInfo.setTotal(countData.getInt("count"));
            return tableDataInfo;
        } else {
            throw new ServiceException(EntityUtils.toString(response.getEntity()));
        }
    }

    @Override
    public boolean insertVector(String indexName, VectorData vectorData) throws IOException {
        Request request = new Request("POST", "/"+indexName+"/_doc");
        JSONObject postData = vectorData.toJson();
        List<BigDecimal> embeddingData = this.embedding(vectorData.getContent());
        postData.set("content_vector", embeddingData);
        request.setJsonEntity(postData.toString());
        Response response = esClient.performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject responseJson = JSONUtil.parseObj(responseBody);
        String result = responseJson.getStr("result");
        return result.equals("created");
    }

    @Override
    public VectorData getVector(String indexName, String id) throws IOException {
        Request request = new Request("GET", "/"+indexName+"/_doc/"+id);
        Response response = esClient.performRequest(request);
        JSONObject responseJson = JSONUtil.parseObj(EntityUtils.toString(response.getEntity()));
        if (responseJson.getBool("found")) {
            JSONObject data = responseJson.getJSONObject("_source");
            VectorData vectorData = new VectorData(id,
                    data.getStr("content"),
                    data.getLong("content_word_count"),
                    data.getLong("category_id"),
                    data.getLong("document_id"),
                    0);
            return vectorData;
        }
        return null;
    }

    @Override
    public boolean updateVector(String indexName, VectorData vectorData) throws IOException {
        Request request = new Request("POST", "/"+indexName+"/_update/"+vectorData.getId());
        JSONObject body = new JSONObject();
        JSONObject doc = vectorData.toJson();
        doc.remove("_id");
        List<BigDecimal> embeddingData = this.embedding(vectorData.getContent());
        doc.set("content_vector", embeddingData);
        body.set("doc", doc);
        request.setJsonEntity(body.toString());
        Response response = esClient.performRequest(request);
        JSONObject responseJson = JSONUtil.parseObj(EntityUtils.toString(response.getEntity()));
        String result = responseJson.getStr("result");
        return result.equals("updated");
    }

    @Override
    public List<VectorData> listByIds(String indexName, List<String> ids) throws IOException {
        Request request = new Request("GET","/"+indexName+"/_mget");
        List<JSONObject> docs = new ArrayList<>();
        for (String id : ids) {
            JSONObject item = new JSONObject();
            item.set("_index", indexName);
            item.set("_id", id);
            docs.add(item);
        }
        JSONObject body = new JSONObject();
        body.set("docs",docs);
        request.setJsonEntity(body.toString());

        Response response = esClient.performRequest(request);
        JSONObject responseJson = JSONUtil.parseObj(EntityUtils.toString(response.getEntity()));
        List<JSONObject> list = responseJson.getBeanList("docs", JSONObject.class);
        List<VectorData> results = new ArrayList<>();
        for (JSONObject data : list) {
            JSONObject source = data.getJSONObject("_source");
            if (source != null) {
                VectorData vectorData = new VectorData(data.getStr("_id"),
                        source.getStr("content"), source.getLong("content_word_count"),
                        source.getLong("category_id"), source.getLong("document_id"),
                        0
                );
                results.add(vectorData);
            }
        }
        return results;
    }

    @Override
    public boolean deleteVector(String indexName, List<String> ids) throws IOException {
        Request request = new Request("POST", "/_bulk");
        StringBuilder sb = new StringBuilder();
        for (String id : ids) {
            JSONObject delete = new JSONObject();
            JSONObject item = new JSONObject();
            item.set("_id", id);
            item.set("_index", indexName);
            delete.set("delete", item);
            sb.append(delete).append("\n");
        }
        request.setJsonEntity(sb.toString());
        Response response = esClient.performRequest(request);
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode == 200;
    }
}
