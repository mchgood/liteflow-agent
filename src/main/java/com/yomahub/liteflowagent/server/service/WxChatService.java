package com.yomahub.liteflowagent.server.service;

import com.yomahub.liteflowagent.server.dto.ChatDto;

public interface WxChatService {

    String asyncChat(String userId, String msg);

    ChatDto getChatResult(String msgId);
}
