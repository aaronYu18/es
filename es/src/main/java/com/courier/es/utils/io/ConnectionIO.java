package com.courier.es.utils.io;

import java.net.UnknownHostException;

/**
 * Created by Ryan
 */
public interface ConnectionIO {

    public void init() throws UnknownHostException;

    public boolean connect();

    public boolean disconnect();
}
