package cn.com.chinarecrm.rop.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;

import cn.com.chinarecrm.rop.core.signer.AppsecretFetcher;
import cn.com.chinarecrm.rop.core.signer.DefaultMD5Fetcher;
import cn.com.chinarecrm.rop.server.BufferResponse;
import cn.com.chinarecrm.rop.server.NullRequestChecker;
import cn.com.chinarecrm.rop.server.ROPServlet;
import cn.com.chinarecrm.rop.server.ROPSignInterceptor;
import cn.com.chinarecrm.rop.server.RequestChecker;
import cn.com.chinarecrm.rop.server.ResettableStreamHttpServletRequest;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
@Configuration
@EnableConfigurationProperties(ROPServerConfigurationProperties.class)
public class ROPServerAutoConfiguration {

    /**
     * 没有的时候垫底的存在,不建议使用,请自行实现AppsecretFetcher并声明为bean
     *
     * @return 默认 AppsecretFetcher
     */
    @Bean
    @ConditionalOnMissingBean(AppsecretFetcher.class)
    public AppsecretFetcher appsecretFetcher() {
        return new DefaultMD5Fetcher();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(ROPServerConfigurationProperties properties,
                                                         MultipartResolver multipartResolver) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Filter() {

            @Override
            public void destroy() {
                // 兼容低版本
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                ServletRequest requestWrapper = null;

                if (request instanceof HttpServletRequest) {
                    requestWrapper = new ResettableStreamHttpServletRequest((HttpServletRequest) request);
                }
                if (requestWrapper == null || multipartResolver.isMultipart((HttpServletRequest) request)) {
                    chain.doFilter(request, response);
                } else {
                    chain.doFilter(requestWrapper, response);
                }
                HttpServletResponse resp = (HttpServletResponse) response;
                BufferResponse myresponse = new BufferResponse((HttpServletResponse)resp);
                byte[] out = myresponse.getBuffer();
                
                System.err.println(Lang.md5(new ByteArrayInputStream(out)));
                resp.addHeader("s", "1");
                response.getOutputStream().write(out);
            }

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                // 兼容低版本
            }
        });
        registration.addUrlPatterns(properties.getRopPath());
        registration.setOrder(1);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean(RequestChecker.class)
    public RequestChecker requestChecker() {
        return new NullRequestChecker();
    }

    @Bean
    public ROPSignInterceptor ropSignInterceptor(ROPServerConfigurationProperties properties) {
        return new ROPSignInterceptor(properties.getDigestName());
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(ROPServerConfigurationProperties properties,
                                                           MultipartConfigElement multipartConfigFactory) {
        ServletRegistrationBean ropServletRegistrationBean = new ServletRegistrationBean(new ROPServlet());
        ropServletRegistrationBean.addUrlMappings(properties.getRopPath());
        ropServletRegistrationBean.setMultipartConfig(multipartConfigFactory);
        NutMap initParameters = NutMap.NEW().setv("timeout", "" + properties.getTimeout());
        if (Strings.isNotBlank(properties.getGateWayUri())) {
            initParameters.setv("gateWayUri", properties.getGateWayUri());
        }
        ropServletRegistrationBean.setInitParameters(initParameters);
        return ropServletRegistrationBean;
    }

}
