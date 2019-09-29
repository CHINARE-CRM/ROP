package cn.com.chinarecrm.rop.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.http.Cookie;
import org.nutz.http.Header;
import org.nutz.http.Http;
import org.nutz.http.Request.METHOD;
import org.nutz.json.Json;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.Encoding;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.util.NutMap;

import cn.com.chinarecrm.rop.core.signer.SignerHelper;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class ROPRequest {
    public static ROPRequest create(String gateway, METHOD method) {
        return create(gateway, method, new HashMap<String, Object>());
    }

    public static ROPRequest create(String gateway, METHOD method, Map<String, Object> params) {
        return ROPRequest.create(gateway, method, params, Header.create());
    }

    public static ROPRequest create(String gateway, METHOD method, Map<String, Object> params, Header header) {
        return new ROPRequest().setMethod(method).setParams(params).setGateway(gateway).setHeader(header);
    }

    public static ROPRequest create(String gateway, METHOD method, String paramsAsJson) {
        return create(gateway, method, (Map<String, Object>) Json.fromJson(paramsAsJson));
    }

    public static ROPRequest create(String gateway, METHOD method, String paramsAsJson, Header header) {
        return create(gateway, method, (Map<String, Object>) Json.fromJson(paramsAsJson), header);
    }

    public static ROPRequest get(String gateway) {
        return create(gateway, METHOD.GET, new HashMap<String, Object>());
    }

    public static ROPRequest get(String gateway, Header header) {
        return ROPRequest.create(gateway, METHOD.GET, new HashMap<String, Object>(), header);
    }

    public static ROPRequest post(String gateway) {
        return create(gateway, METHOD.POST, new HashMap<String, Object>());
    }

    public static ROPRequest post(String gateway, Header header) {
        return ROPRequest.create(gateway, METHOD.POST, new HashMap<String, Object>(), header);
    }

    private byte[] data;

    private String enc = Encoding.UTF8;
    private String gateway;
    private Header header;
    private InputStream inputStream;
    private METHOD method;
    private Map<String, Object> params;

    private ROPRequest() {}

    protected void fileUpload(final StringBuilder sb) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        for (final String key : keys) {
            Object val = params.get(key);
            if (val == null)
                val = "";
            Lang.each(val, new Each<Object>() {
                @Override
                public void invoke(int index, Object ele, int length)
                        throws ExitLoop, ContinueLoop, LoopException {
                    if (ele instanceof File) {
                        sb.append(Http.encode(key, enc))
                          .append('=')
                          .append(Http.encode(Lang.md5((File) ele), enc))
                          .append('&');
                    } else {
                        sb.append(Http.encode(key, enc))
                          .append('=')
                          .append(Http.encode(ele, enc))
                          .append('&');
                    }
                }
            });
        }
    }

    public Cookie getCookie() {
        String s = header.get("Cookie");
        if (null == s)
            return new Cookie();
        return new Cookie(s);
    }

    public byte[] getData() {
        return data;
    }

    public String getEnc() {
        return enc;
    }

    /**
     * @return the gateway
     */
    public String getGateway() {
        return gateway;
    }

    public Header getHeader() {
        return header;
    }

    public InputStream getInputStream() {
        if (inputStream != null) {
            return inputStream;
        } else {
            if (header.get("Content-Type") == null)
                header.asFormContentType(enc);
            if (null == data) {
                try {
                    return new ByteArrayInputStream(getURLEncodedParams().getBytes(enc));
                }
                catch (UnsupportedEncodingException e) {
                    throw Lang.wrapThrow(e);
                }
            }
            return new ByteArrayInputStream(data);
        }
    }

    public METHOD getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getURLEncodedParams() {
        // 此处不影响发送的数据,只影响签名,需要按照rop的规则进行签名串的获取即可
        final StringBuilder sb = new StringBuilder();
        if (isFileUpload()) {// 文件上传的签名流
            fileUpload(sb);
        } else if (params != null) {
            return SignerHelper.mapAsUrlParams(params, enc);
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public ROPRequest header(String key, String value) {
        getHeader().set(key, value);
        return this;
    }

    public boolean isDelete() {
        return METHOD.DELETE == method;
    }

    public boolean isFileUpload() {
        final NutMap t = NutMap.NEW().addv("target", false);
        if ((isPost() || isPut()) && getParams() != null) {
            for (Object val : getParams().values()) {
                Lang.each(val, new Each<Object>() {

                    @Override
                    public void invoke(int index, Object ele, int length) throws ExitLoop, ContinueLoop, LoopException {
                        if (ele instanceof File) {
                            t.put("target", true);
                            throw new ExitLoop();
                        }
                    }
                });
            }
        }
        return t.getBoolean("target");
    }

    public boolean isGet() {
        return METHOD.GET == method;
    }

    public boolean isPost() {
        return METHOD.POST == method;
    }

    public boolean isPut() {
        return METHOD.PUT == method;
    }

    public ROPRequest setCookie(Cookie cookie) {
        header.set("Cookie", cookie.toString());
        return this;
    }

    public ROPRequest setData(byte[] data) {
        this.data = data;
        return this;
    }

    public ROPRequest setData(String data) {
        this.data = data.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public ROPRequest setEnc(String reqEnc) {
        if (reqEnc != null)
            this.enc = reqEnc;
        return this;
    }

    public ROPRequest setGateway(String gateway) {
        this.gateway = gateway;
        return this;
    }

    public ROPRequest setHeader(Header header) {
        if (header == null)
            header = Header.create();
        this.header = header;
        return this;
    }

    public ROPRequest setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public ROPRequest setMethod(METHOD method) {
        this.method = method;
        return this;
    }

    public ROPRequest setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }
}
