package cn.com.chinarecrm.rop.core.signer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.upload.UploadingContext;

import cn.com.chinarecrm.rop.ROPConfig;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public abstract class AbstractSinger implements Signer {
    UploadingContext context = new UploadingContext(System.getProperty("java.io.tmpdir"));

    Log log = Logs.get();

    @Override
    public boolean check(HttpServletRequest request, AppsecretFetcher fetcher) {
        if (Strings.isBlank(request.getHeader(ROPConfig.APP_KEY_KEY))
            || Strings.isBlank(request.getHeader(ROPConfig.TS_KEY))
            || Strings.isBlank(request.getHeader(ROPConfig.METHOD_KEY))
            || Strings.isBlank(request.getHeader(ROPConfig.NONCE_KEY))
            || Strings.isBlank(request.getHeader(ROPConfig.SIGN_KEY))) {
            return false;
        }
        String sign = request.getHeader(ROPConfig.SIGN_KEY);
        log.debugf("Expected sign is %s", sign);
        return Strings.equalsIgnoreCase(sign(request, fetcher), sign);
    }

    public String contentType(HttpServletRequest request) {
        return request.getHeader("Content-Type");
    }

    protected String getDataMate(HttpServletRequest request) {
        if (Strings.equalsIgnoreCase(request.getMethod(), "GET")) {// GET请求需要处理一下
            return Lang.md5(request.getQueryString());
        }
        // 文件上传
        if (isFileUpload(request)) {
            try {
                return Lang.md5(new ByteArrayInputStream(
                                                         getURLEncodedParams(request).getBytes(request.getCharacterEncoding())));
            }
            catch (IOException | ServletException e) {
                log.debug("不支持的编码!");
                throw Lang.wrapThrow(e);
            }
        }
        try {
            StringBuilder info = Streams.read(new InputStreamReader(request.getInputStream()));
            if (info.length() == 0) {
                return Lang.md5(
                                SignerHelper.paramMapAsUrlString(request.getParameterMap(), request.getCharacterEncoding()));
            }
            return Lang.md5(info);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    public String getURLEncodedParams(final HttpServletRequest request) throws IOException, ServletException {
        final StringBuilder sb = new StringBuilder();
        List<Part> parts = Lang.collection2list(request.getParts());
        Collections.sort(parts, (part1, part2) -> part1.getName().compareTo(part2.getName()));
        parts.stream().forEach(part -> {
            String key = part.getName();
            if (Strings.isBlank(part.getContentType())) {
                // 参数
                sb.append(Http.encode(key, request.getCharacterEncoding()))
                  .append('=')
                  .append(Http.encode(request.getParameter(key), request.getCharacterEncoding()))
                  .append('&');
            } else {
                // 文件
                try {
                    sb.append(Http.encode(key, request.getCharacterEncoding()))
                      .append('=')
                      .append(Http.encode(Lang.md5(part.getInputStream()), request.getCharacterEncoding()))
                      .append('&');
                }
                catch (IOException e) {
                    throw Lang.wrapThrow(e);
                }
            }
        });

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();

    }

    private boolean isCommonFileUpload(HttpServletRequest request) {
        return contentType(request) != null
               && contentType(request).startsWith("multipart/form-data");
    }

    /**
     * @param request
     * @return
     */
    private boolean isFileUpload(HttpServletRequest request) {
        return isCommonFileUpload(request) || isHtml5FileUpload(request);
    }

    private boolean isHtml5FileUpload(HttpServletRequest request) {
        return contentType(request) != null
               && contentType(request).startsWith("application/octet-stream");
    }

    /**
     * 根据appSecret获取器签名
     *
     * @param fetcher
     *            appSecret获取器
     * @param appKey
     *            应用key
     * @param timestamp
     *            时间戳
     * @param gateway
     *            方法/路由
     * @param nonce
     *            随机串
     * @param dataMate
     *            数据元数据
     * @return 签名字符串
     */
    public String sign(AppsecretFetcher fetcher,
                       String appKey,
                       String timestamp,
                       String gateway,
                       String nonce,
                       String dataMate) {
        return sign(fetcher.fetch(appKey), timestamp, gateway, nonce, dataMate);
    }

    /**
     * 服务器端签名
     *
     * @param request
     *            请求
     * @param fetcher
     *            appSecret获取器
     * @return 签名字符串
     */
    public String sign(HttpServletRequest request, AppsecretFetcher fetcher) {
        return sign(fetcher,
                    request.getHeader(ROPConfig.APP_KEY_KEY),
                    request.getHeader(ROPConfig.TS_KEY),
                    request.getHeader(ROPConfig.METHOD_KEY),
                    request.getHeader(ROPConfig.NONCE_KEY),
                    getDataMate(request));
    }
    
    @Override
    public String sign(HttpServletResponse response, String appSecret,String timestamp,String gateway,String nonce) {
    	return sign(appSecret, timestamp, gateway, nonce, getDataMate(response));
    }
    
    private String getDataMate(HttpServletResponse response) {
		return null;
	}

	@Override
    public boolean check(Response response, String appKey, String appSecret) {
    	return false;
    }

}
