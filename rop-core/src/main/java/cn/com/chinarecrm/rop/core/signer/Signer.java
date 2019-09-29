package cn.com.chinarecrm.rop.core.signer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public interface Signer {
    /**
     * 签名检查
     *
     * @param request
     *            请求
     * @param fetcher
     *            密钥获取器
     * @return 是否检查通过
     */
    public boolean check(HttpServletRequest request, AppsecretFetcher fetcher);

    /**
     * 名称
     *
     * @return 签名器名称
     */
    public String name();

    /**
     *
     * @param appSecret
     *            密钥
     * @param timestamp
     *            时间戳
     * @param gateway
     *            网关/方法名称
     * @param nonce
     *            随机串
     * @param dataMate
     *            数据元数据
     * @return 签名字符串
     */
    public String sign(String appSecret, String timestamp, String gateway, String nonce, String dataMate);

}
