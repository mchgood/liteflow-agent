package com.yomahub.liteflowagent.server.api;

public interface ZhiPuApi {

    /**
     * 创建新会话
     *
     * @return 返回会话 id
     */
    String createConversation();

    /**
     * 创建聊天
     *
     * @param conversationId 会话 id
     * @param content        聊天内容
     * @return 返回一个聊天结果 id
     */
    String createChat(String traceIs, String conversationId, String content);
}
