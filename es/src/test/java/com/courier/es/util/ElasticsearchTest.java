package com.courier.es.util;

import com.courier.es.temp.ISearch;
import com.courier.es.temp.SearchImpl;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2016/12/14.
 */
public class ElasticsearchTest {
    ISearch search;

    @Test
    public void get() {
        Map<String,Object> map = search.get("a", "type1", "123");
        Set<String> set = map.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            String key = it.next();
            System.out.println(key + ":" + map.get(key));
        }
    }

    @Test
    public void del() {
        search.delete("a", "type1", "123");
    }

    @Test
    public void save() {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("k1", "v1");
        values.put("k2", "v2");

        Map<String,Object> map = search.save("a", "type1", "123", values);
        Set<String> set = map.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            String key = it.next();
            System.out.println(key + ":" + map.get(key));
        }

    }

    @Before
    public void before() {
        search = new SearchImpl();
    }
}


