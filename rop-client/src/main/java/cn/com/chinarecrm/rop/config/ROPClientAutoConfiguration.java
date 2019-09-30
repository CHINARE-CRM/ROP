package cn.com.chinarecrm.rop.config;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.com.chinarecrm.rop.client.ROPClient;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
@Configuration
@EnableConfigurationProperties(ROPClientConfigurationProperties.class)
public class ROPClientAutoConfiguration {

    @Bean
    public ROPClient ropClient(ROPClientConfigurationProperties configProperties) {
        ROPClient client = ROPClient.create(configProperties.getAppKey(),
                                            configProperties.getAppSecret(),
                                            configProperties.getEndpoint(),
                                            configProperties.getDigestName());
        if (configProperties.getProxy().isEnable()) {
            Proxy proxy = new Proxy(configProperties.getProxy().getType(),
                                    new InetSocketAddress(configProperties.getProxy().getHost(), configProperties.getProxy().getPort()));
            client.setProxy(proxy);
        }
        return client;
    }
}
