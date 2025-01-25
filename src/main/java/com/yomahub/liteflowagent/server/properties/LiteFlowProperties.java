package com.yomahub.liteflowagent.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "liteflow.agent")
public class LiteFlowProperties {

    private String server;
}
