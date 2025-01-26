package com.yomahub.liteflowagent.server.controller;

import cn.hutool.core.lang.Assert;
import com.yomahub.liteflowagent.server.dto.ChatDto;
import com.yomahub.liteflowagent.server.service.WxChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatController {
    @Autowired
    private WxChatService wxChatService;

    @GetMapping("/{msgId}")
    public ChatDto getChatResult(@PathVariable("msgId") String msgId) {
        Assert.notBlank(msgId);
        return wxChatService.getChatResult(msgId);
    }
}
