package com.courier.es.util;

import com.courier.es.utils.es.EsUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2016/12/15.
 */
@ContextConfiguration(locations = {"classpath*:application*Context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class EsUtilTest {
    @Autowired
    private EsUtils esUtils;

    @Test
    public void get() {
        Map<String,Object> map = esUtils.get("a", "type1", "123");
        Set<String> set = map.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            String key = it.next();
            System.out.println(key + ":" + map.get(key));
        }
    }

    @Test
    public void del() {
        esUtils.delete("a", "type1", "123");
    }

    @Test
    public void save() {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("k1", "v1");
        values.put("k2", "v2");

        Map<String,Object> map = esUtils.save("a", "type1", "123", values);
        Set<String> set = map.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            String key = it.next();
            System.out.println(key + ":" + map.get(key));
        }

    }
}
