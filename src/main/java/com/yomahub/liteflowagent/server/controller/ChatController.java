package com.yomahub.liteflowagent.server.controller;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflowagent.server.service.WxChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/")
public class ChatController {
    @Autowired
    private WxChatService wxChatService;

    @GetMapping("/{msgId}")
    public String getChatResult(@PathVariable("msgId") String msgId) {
        if (StrUtil.isBlank(msgId)){
            return "msgId 不能为空";
        }
        return wxChatService.getChatResult(msgId);
    }
}
