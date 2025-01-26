package com.yomahub.liteflowagent.server.service.impl;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.Assert;
import com.yomahub.liteflowagent.server.api.ZhiPuApi;
import com.yomahub.liteflowagent.server.dto.ChatDto;
import com.yomahub.liteflowagent.server.exception.AuthLimitException;
import com.yomahub.liteflowagent.server.mapper.ChatPoMapper;
import com.yomahub.liteflowagent.server.po.ChatPo;
import com.yomahub.liteflowagent.server.properties.AuthProperties;
import com.yomahub.liteflowagent.server.service.WxChatService;
import com.yomahub.liteflowagent.server.util.ThreadPlusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class WxChatServiceImpl implements WxChatService {
    private static final ThreadPoolExecutor CHAT_THREAD_POOL = ThreadPlusUtil.getChatThreadPool();
    @Autowired
    private ZhiPuApi zhiPuApi;
    @Autowired
    private AuthProperties authProperties;
    @Autowired
    private ChatPoMapper chatPoMapper;
    private final Cache<String, Integer> USER_VISIT_CACHE = CacheUtil.newLFUCache(128, 60 * 60 * 1000);

    @Override
    public String asyncChat(String userId, String msg) {
        // 1. 检查用户访问次数
        Integer count = USER_VISIT_CACHE.get(userId, () -> {
            Long visitCount = chatPoMapper.selectVisitTodayCount(userId);
            return visitCount.intValue();
        });
        count++;
        // 检查用户访问次数是否超限，白名单用户不过滤
        if (count > authProperties.getDailyLimitCount() && !authProperties.getWhiteUserIdSet().contains(userId)) {
            throw new AuthLimitException("你今天的访问次数已经用完了,限制每日访问" + authProperties.getDailyLimitCount() + "次");
        }
        USER_VISIT_CACHE.put(userId, count);

        // 2. 创建聊天
        String conversationId = zhiPuApi.createConversation();

        // 3. 异步聊天
        ChatDto chatDto = new ChatDto()
                .setUserId(userId)
                .setConversationId(conversationId)
                .setQuestion(msg)
                .setAnswer("正在为您查询，请稍后");

        CHAT_THREAD_POOL.execute(() -> {
            try {
                String result = zhiPuApi.createChat(conversationId, conversationId, msg);
                chatPoMapper.updateAnswer(conversationId, result);
            } catch (Exception ex) {
                chatPoMapper.updateAnswer(conversationId, "聊天异常，请稍后再试");
                log.error("chat error", ex);
            }
        });

        chatPoMapper.saveByChatDto(chatDto);

        return conversationId;
    }

    @Override
    public ChatDto getChatResult(String msgId) {
        ChatPo chatPo = chatPoMapper.selectByConversationId(msgId);
        Assert.notNull(chatPo);
        return ChatDto.convert(chatPo);
    }
}
