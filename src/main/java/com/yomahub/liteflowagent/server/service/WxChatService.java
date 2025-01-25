package com.yomahub.liteflowagent.server.service;

public interface WxChatService {

    String asyncChat(String userId, String msg);

    String getChatResult(String msgId);
}
