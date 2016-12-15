package com.courier.es.utils.es;

import com.courier.es.utils.io.ConnectionIO;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;


public class EsClusterClient implements ConnectionIO {

    public static final Logger logger = LoggerFactory.getLogger(EsClusterClient.class);

    private TransportClient client = null;
    private volatile boolean inited = false;
    Settings.Builder builder = Settings.builder();
    private Set<InetSocketTransportAddress> addresses = new HashSet<>();

    private EsConfig esConfig;
    @NotNull
    private String ips;
    @NotNull
    private String clusterName;
    @NotNull
    private Boolean sniff;

    @PostConstruct
    @Override
    public synchronized void init() throws UnknownHostException {
        if(inited) return;

        if(StringUtils.isEmpty(ips)){
            logger.error("es cluster ips is empty");
            return;
        }

        //  集群名称
        if(!StringUtils.isEmpty(clusterName)) builder.put("cluster.name", clusterName);
        // 是否启用嗅探
        if(null != sniff) builder.put("client.transport.sniff", sniff);

        try {
            String[] ipArrays = ips.split(",");
            for (String ipPort : ipArrays){
                String[] hostAndPort = ipPort.split(":");
                InetSocketTransportAddress address = new InetSocketTransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1]));
                addresses.add(address);
            }
            this.inited = connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error(String.format("init elasticSearch client err:=>msg:[%s]", e.getMessage()), e);
            if (client != null) this.client.close();
        }
    }

    @Override
    public boolean connect() {
        client = new PreBuiltTransportClient(builder.build());

        if(addresses == null || addresses.size() == 0) {
            logger.info("connect redis pool error, ips is empty");
            return false;
        }
        for (InetSocketTransportAddress address : addresses)
            client.addTransportAddresses(address);

        logger.info("connect redis pool success");
        return true;
    }

    @PreDestroy
    @Override
    public synchronized boolean disconnect() {
        if(client != null){
            client.close();
            logger.info("disconnect elasticSearch success");
            return true;
        }
        return false;
    }


    public EsConfig getEsConfig() {
        return esConfig;
    }

    public void setEsConfig(EsConfig esConfig) {
        this.esConfig = esConfig;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Boolean getSniff() {
        return sniff;
    }

    public void setSniff(Boolean sniff) {
        this.sniff = sniff;
    }
    public TransportClient get() {
        return this.client;
    }
}
