package com.yomahub.liteflowagent.server.config;

import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import com.yomahub.liteflowagent.server.wx.handler.LogHandler;
import com.yomahub.liteflowagent.server.wx.handler.MsgHandler;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpPlusConfiguration {


    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService, LogHandler logHandler, MsgHandler msgHandler) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        //  记录所有事件的日志 （异步执行）
        newRouter.rule().handler(logHandler).next();
        //  默认
        newRouter.rule().async(false).handler(msgHandler).end();

        return newRouter;
    }

}
