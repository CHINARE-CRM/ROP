package cn.com.chinarecrm.rop.config;

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
        return ROPClient.create(configProperties.getAppKey(),
                                configProperties.getAppSecret(),
                                configProperties.getEndpoint(),
                                configProperties.getDigestName());
    }
}
