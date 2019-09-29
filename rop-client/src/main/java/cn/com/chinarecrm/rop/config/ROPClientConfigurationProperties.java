package cn.com.chinarecrm.rop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
@ConfigurationProperties(prefix = "rop.client")
public class ROPClientConfigurationProperties {
    /**
     * appKey
     */
    String appKey;
    /**
     * appSecret
     */
    String appSecret;
    /**
     * 签名算法
     */
    String digestName;
    /**
     * 对方接口地址
     */
    String endpoint;

    public String getAppKey() {
        return appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getDigestName() {
        return digestName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setDigestName(String digestName) {
        this.digestName = digestName;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

}
