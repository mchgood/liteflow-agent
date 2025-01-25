package com.yomahub.liteflowagent.server.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "zhipu")
public class ZhiPuProperties {
    /**
     * api key
     */
    private String apiKey;

    /**
     * 智能体 id
     */
    private String appId;
}
