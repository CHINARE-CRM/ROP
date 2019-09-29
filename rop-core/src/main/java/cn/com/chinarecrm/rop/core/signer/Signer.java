package cn.com.chinarecrm.rop.core.signer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.http.Response;

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
     *  -对 response 进行签名
     * @param response 响应
     * @param fetcher  密钥获取器
     * @return 签名值
     */
    public String sign(HttpServletResponse response, String appSecret,String timestamp,String gateway,String nonce) ;
    
    public boolean check(Response response,String appKey,String appSecret);

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
