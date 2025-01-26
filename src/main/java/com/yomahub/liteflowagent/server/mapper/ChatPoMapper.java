package com.yomahub.liteflowagent.server.mapper;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yomahub.liteflowagent.server.dto.ChatDto;
import com.yomahub.liteflowagent.server.po.ChatPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatPoMapper extends BaseMapper<ChatPo> {

    /**
     * 查询当天访问次数
     *
     * @param userId 用户 id
     */
    default Long selectVisitTodayCount(String userId) {
        String todayStart = DateUtil.today() + " 00:00:00";
        LambdaQueryWrapper<ChatPo> wrapper = new LambdaQueryWrapper<ChatPo>();
        wrapper.gt(ChatPo::getCreateTime, DateUtil.parse(todayStart))
                .eq(ChatPo::getUserId, userId);

        return selectCount(wrapper);
    }

    /**
     * 更新回答
     *
     * @param conversationId 对话 id
     * @param answer         回答
     */
    default void updateAnswer(String conversationId, String answer) {
        LambdaUpdateWrapper<ChatPo> wrapper = new LambdaUpdateWrapper<ChatPo>();
        wrapper.eq(ChatPo::getConversationId, conversationId)
                .set(ChatPo::getUpdateTime, DateUtil.date())
                .set(ChatPo::getAnswer, answer);
        update(wrapper);
    }

    /**
     * 保存
     */
    default void saveByChatDto(ChatDto chatDto) {
        ChatPo chatPo = ChatDto.convert(chatDto);
        insert(chatPo);
    }

    /**
     * 根据对话id查询
     *
     * @param conversationId 对话id
     */
    default ChatPo selectByConversationId(String conversationId) {
        LambdaQueryWrapper<ChatPo> wrapper = new LambdaQueryWrapper<ChatPo>();
        wrapper.eq(ChatPo::getConversationId, conversationId);
        return selectOne(wrapper);
    }
}