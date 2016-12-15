package com.courier.es.utils.es;

import com.courier.es.utils.io.ConnectionConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by user on 2016/12/15.
 */
public class EsConfig implements ConnectionConfig {
    public static final Logger logger = LoggerFactory.getLogger(EsConfig.class);
    @PostConstruct
    @Override
    public void init() {
        logger.info("begin loading es config properties...");
    }
}
