package com.yomahub.liteflowagent.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "t_chat")
public class ChatPo {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 问题
     */
    @TableField(value = "question")
    private String question;

    /**
     * 回答
     */
    @TableField(value = "answer")
    private String answer;

    /**
     * 问答 id
     */
    @TableField(value = "conversation_id")
    private String conversationId;

    /**
     * 用户 id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}