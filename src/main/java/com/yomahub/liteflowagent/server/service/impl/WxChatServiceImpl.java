package com.yomahub.liteflowagent.server.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflowagent.server.api.ZhiPuApi;
import com.yomahub.liteflowagent.server.exception.AuthLimitException;
import com.yomahub.liteflowagent.server.properties.AuthProperties;
import com.yomahub.liteflowagent.server.service.WxChatService;
import com.yomahub.liteflowagent.server.util.ThreadPlusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WxChatServiceImpl implements WxChatService {
    private static final ThreadPoolExecutor CHAT_THREAD_POOL = ThreadPlusUtil.getChatThreadPool();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ZhiPuApi zhiPuApi;
    @Autowired
    private AuthProperties authProperties;

    @Override
    public String asyncChat(String userId, String msg) {
        // 1. 检查用户访问次数
        String userLimitRedisKey = buildUserLimitKey(userId);
        Long count = stringRedisTemplate.opsForValue().increment(userLimitRedisKey, 1);
        if (count == null) {
            count = 1L;
        }
        Long expireTime = stringRedisTemplate.getExpire(userLimitRedisKey);
        if (expireTime == null || expireTime == -1) {
            stringRedisTemplate.expire(userLimitRedisKey, 1, TimeUnit.DAYS);
        }
        //   检查用户访问次数是否超限，白名单用户不过滤
        if (count > authProperties.getDailyLimitCount() && !authProperties.getWhiteUserIdSet().contains(userId)) {
            throw new AuthLimitException("你今天的访问次数已经用完了,限制每日访问" + authProperties.getDailyLimitCount() + "次");
        }

        // 2. 创建聊天
        String conversationId = zhiPuApi.createConversation();

        // 3. 异步聊天
        CHAT_THREAD_POOL.execute(() -> {
            String chatResultKey = buildChatResultKey(conversationId);
            try {
                String result = zhiPuApi.createChat(conversationId, conversationId, msg);
                stringRedisTemplate.opsForValue().set(chatResultKey, result, 1, TimeUnit.HOURS);
            } catch (Exception ex) {
                stringRedisTemplate.opsForValue().set(chatResultKey, "聊天异常，请稍后再试", 1, TimeUnit.HOURS);
                log.error("chat error", ex);
            }
        });
        return conversationId;
    }

    @Override
    public String getChatResult(String msgId) {
        String chatResultKey = buildChatResultKey(msgId);
        String result = stringRedisTemplate.opsForValue().get(chatResultKey);
        if (StrUtil.isBlankIfStr(result)) {
            return "答案正在搜索，稍后查看";
        }
        return result;
    }

    private String buildUserLimitKey(String userId) {
        return "chat:user:limit:" + userId;
    }

    private String buildChatResultKey(String msgId) {
        return "chat:user:msgId:" + msgId;
    }
}
