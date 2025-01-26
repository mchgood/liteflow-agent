package com.yomahub.liteflowagent.server.dto;

import com.yomahub.liteflowagent.server.po.ChatPo;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

@Data
@Accessors(chain = true)
public class ChatDto {
    /**
     * 用户 id
     */
    private String userId;
    /**
     * 对话 id
     */
    private String conversationId;
    /**
     * 问题
     */
    private String question;
    /**
     * 答案
     */
    private String answer;

    public static ChatPo convert(ChatDto chatDto) {
        ChatPo po = new ChatPo();
        BeanUtils.copyProperties(chatDto, po);
        return po;
    }

    public static ChatDto convert(ChatPo po) {
        ChatDto dto = new ChatDto();
        BeanUtils.copyProperties(po, dto);
        return dto;
    }
}
