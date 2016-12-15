package com.courier.es.temp;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.engine.VersionConflictEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by user on 2016/12/14.
 */
public class SearchImpl implements ISearch {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchImpl.class);

    private SearchClientProvider searchClientProvider;
    public  SearchImpl() {
        searchClientProvider = new SearchClientProvider();
        searchClientProvider.init();
    }

    public Map<String, Object> save(String index, String type, String id, Map<String, Object> data) {
        TransportClient client = searchClientProvider.get();
        IndexRequestBuilder builder = client.prepareIndex(index, type, id);
        IndexResponse response = builder
                .setSource(data)
                .execute()
                .actionGet();
        LOGGER.info("save index:=>index:{}, type:{}, id:{}, data:{}, rsp:{}", index, type, id, data, response);
        return data;
    }

    public int update(String index, String type, String id, Map<String, Object> data) {
        int i = 2;
        do {
            try {
                if (_innerUpdate(index, type, id, data)) return 1;
            } catch (VersionConflictEngineException e) {
                LOGGER.warn(String.format("update index:=>index:%s, type:%s, id:%s, data:%s, rsp:%s",
                        index, type, id, data, e.getMessage()), e);
            }
        } while ((i--) > 0);
        return _innerUpdate(index, type, id, data) ? 1 : 0;
    }

    public int delete(String index, String type, String id) {
        TransportClient client = searchClientProvider.get();
        DeleteRequestBuilder builder = client.prepareDelete(index, type, id);
        DeleteResponse response = builder.execute().actionGet();

       /* DeleteResponse response = client.prepareDelete("twitter", "tweet", "1")
                .get();*/
        LOGGER.info("delete index:=>index:{}, type:{}, id:{}, rsp:{}", index, type, id, response);
        return response.status().getStatus();
    }

    public Map<String, Object> get(String index, String type, String id) {
        TransportClient client = searchClientProvider.get();
        GetRequestBuilder builder = client.prepareGet(index, type, id);
        GetResponse response = builder.execute().actionGet();
        return response.isExists() ? response.getSource() : null;
    }

    private boolean _innerUpdate(String index, String type, String id, Map<String, Object> data) {
        TransportClient client = searchClientProvider.get();
        GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
        GetResponse getResponse = getRequestBuilder.execute().actionGet();
        if (getResponse.isExists()) {
            final long version = getResponse.getVersion();
            final Map<String, Object> source = getResponse.getSource();
            source.putAll(data);

            IndexRequestBuilder builder = client.prepareIndex(index, type, id);
            IndexResponse response = builder
                    .setVersion(version)
                    .setSource(source)
                    .execute()
                    .actionGet();
            LOGGER.info("update index:=>index:{}, type:{}, id:{}, data:{}, rsp:{}",
                    index, type, id, data, response);
            return true;
        }
        throw new RuntimeException(String.format("can not get document:=>index:%s, type:%s, id:%s ", index, type, id));
    }
}