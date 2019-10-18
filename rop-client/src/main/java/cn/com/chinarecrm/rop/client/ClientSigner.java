package cn.com.chinarecrm.rop.client;

import cn.com.chinarecrm.rop.core.signer.Signer;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public interface ClientSigner extends Signer {
    /**
     * 客户端签名
     *
     * @param appSecret
     *            应用密钥
     * @param timestamp
     *            时间戳
     * @param gateway
     *            方法/路由
     * @param nonce
     *            随机串
     * @param request
     *            请求
     * @return 签名字符串
     */
    public String sign(String appSecret, String timestamp, String gateway, String nonce, ROPRequest request);
}
