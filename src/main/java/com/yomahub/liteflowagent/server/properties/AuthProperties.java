package com.yomahub.liteflowagent.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    /**
     * 每日限制访问数量
     */
    private Long dailyLimitCount = 3L;

    /**
     * 白名单用户id，不受访问限制
     */
    private Set<String> whiteUserIdSet = new HashSet<>();
}
