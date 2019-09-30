package cn.com.chinarecrm.rop.client;

import java.util.stream.Collectors;

import org.nutz.http.Header;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.chinarecrm.rop.ROPConfig;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class ROPClient {

    public static ROPClient create(String appKey, String appSecret, String endpoint, String digestName) {
        ROPClient client = new ROPClient();
        client.setAppKey(appKey);
        client.setAppSecret(appSecret);
        client.setEndpoint(endpoint);
        client.setDigestName(digestName);
        client.setSigner(new ROPClientDigestSigner(digestName));
        return client;
    }

    private String appKey;

    private String appSecret;
    private String digestName;
    private String endpoint;// 调用点

    Log log = Logs.get();

    ClientSigner signer;

    private ROPClient() {}

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

    public ClientSigner getSigner() {
        return signer;
    }

    public Response send(ROPRequest request) {
        Response response = Sender.create(toRequest(request)).send();
        if (!response.isOK()) {
            throw Lang.makeThrow("请求失败,状态码:%d", response.getStatus());
        }
        if (log.isDebugEnabled()) {
            Header header = response.getHeader();
            log.debugf("response headers -> %s",
                       Json.toJson(header.getAll()
                                         .stream()
                                         .map(item -> NutMap.NEW().addv("key", item.getKey()).addv("value", item.getValue()))
                                         .collect(Collectors.toList())));
        }
        if (signer.check(response, appSecret, request.getGateway())) {
            return response;
        }
        throw Lang.makeThrow("响应签名检查失败!");
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

    public void setSigner(ClientSigner signer) {
        this.signer = signer;
    }

    /**
     * 处理header
     *
     * @param request
     * @return
     */
    private Header signHeader(ROPRequest request) {
        String nonce = R.UU16();
        String ts = Times.now().getTime() + "";
        Header header = null;
        if (request.getMethod() == METHOD.GET) {
            String query = request.getURLEncodedParams();
            String method = Strings.isBlank(query) ? request.getGateway() : request.getGateway() + "?" + query;
            header = request.getHeader()
                            .set(ROPConfig.APP_KEY_KEY, appKey)
                            .set(ROPConfig.METHOD_KEY, method)
                            .set(ROPConfig.NONCE_KEY, nonce)
                            .set(ROPConfig.TS_KEY, ts)
                            .set(ROPConfig.SIGN_KEY, signer.sign(appSecret, ts, method, nonce, request));
        } else {
            header = request.getHeader()
                            .set(ROPConfig.APP_KEY_KEY, appKey)
                            .set(ROPConfig.METHOD_KEY, request.getGateway())
                            .set(ROPConfig.NONCE_KEY, nonce)
                            .set(ROPConfig.TS_KEY, ts)
                            .set(ROPConfig.SIGN_KEY, signer.sign(appSecret, ts, request.getGateway(), nonce, request));
        }
        return request.getData() == null || request.getData().length == 0 ? header.asFormContentType() : header.asJsonContentType();
    }

    public Request toRequest(ROPRequest request) {
        Request req = Request.create(endpoint, request.getMethod());
        req.setParams(request.getParams());
        req.setData(request.getData());
        req.setHeader(signHeader(request));
        Header header = req.getHeader();
        log.debugf("send headers %s", header);
        return req;
    }
}
