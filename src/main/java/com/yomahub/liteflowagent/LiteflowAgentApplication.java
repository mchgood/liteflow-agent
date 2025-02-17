package com.yomahub.liteflowagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.yomahub.liteflowagent", "cn.hutool.extra.spring"})
public class LiteflowAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteflowAgentApplication.class, args);
    }

}
