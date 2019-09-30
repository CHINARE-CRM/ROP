package cn.com.chinarecrm.rop.config;

import java.net.Proxy.Type;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class ProxyConfigurationProperties {

    boolean enable;

    String host;

    int port;

    Type type;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Type getType() {
        return type;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
