package com.courier.es.utils.es;

import java.util.Map;

/**
 * Created by user on 2016/12/14.
 */
public interface EsService {
    public Map<String, Object> save(String index, String type, String id, Map<String, Object> data);

    public int update(String index, String type, String id, Map<String, Object> data);

    public int delete(String index, String type, String id);

    public Map<String, Object> get(String index, String type, String id);

}
