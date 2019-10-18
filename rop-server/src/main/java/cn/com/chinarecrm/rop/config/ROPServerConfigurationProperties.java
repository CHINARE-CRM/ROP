package cn.com.chinarecrm.rop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
@ConfigurationProperties(prefix = "rop.server")
public class ROPServerConfigurationProperties {
    /**
     * 签名方式
     */
    String digestName = "SHA1";

    /**
     * 自行处理的gateway
     */
    String gateWayUri;

    /**
     * 接口路径
     */
    String ropPath = "rop.endpoint";

    /**
     * 接口超时时间
     */
    long timeout = 5;
    
    /**
     * 启动重放检查
     */
    boolean enableReplayCheck =  true;

    
    public boolean isEnableReplayCheck() {
		return enableReplayCheck;
	}

	public void setEnableReplayCheck(boolean enableReplayCheck) {
		this.enableReplayCheck = enableReplayCheck;
	}

	public String getDigestName() {
        return digestName;
    }

    public String getGateWayUri() {
        return gateWayUri;
    }

    public String getRopPath() {
        return ropPath.startsWith("/") ? ropPath : "/" + ropPath;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setDigestName(String digestName) {
        this.digestName = digestName;
    }

    public void setGateWayUri(String gateWayUri) {
        this.gateWayUri = gateWayUri;
    }

    public void setRopPath(String ropPath) {
        this.ropPath = ropPath;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
