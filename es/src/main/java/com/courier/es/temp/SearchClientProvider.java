package com.courier.es.temp;

/**
 * Created by user on 2016/12/14.
 */
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class SearchClientProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchClientProvider.class);

    private TransportClient client = null;
    private volatile boolean inited = false;

    public TransportClient get() {
        return this.client;
    }

    @PreDestroy
    public synchronized void close() {
        if (this.client != null) {
            this.client.close();
        }
    }

    @PostConstruct
    public synchronized void init() {
        if (!inited) {
            try {
                Map<String, String> settingConfig = new HashMap<>();
                settingConfig.put("cluster.name", "aaron-cluster");

                Settings settings = Settings.builder().put(settingConfig).build();
                TransportClient client =  new PreBuiltTransportClient(settings);
                this.client = client;

                String[] addresses = {"192.168.5.27:9300"};
                for (String address : addresses) {
                    String[] hostAndPort = address.split(":");
                    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1])));
                }
                this.inited = true;
            } catch (UnknownHostException e) {
                LOGGER.error(String.format("init search client err:=>msg:[%s]", e.getMessage()), e);
                if (client != null) {
                    this.client.close();
                }
            }
        }
    }
}
