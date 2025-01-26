package com.yomahub.liteflowagent.server.wx.handler;

import com.yomahub.liteflowagent.server.exception.BaseException;
import com.yomahub.liteflowagent.server.properties.LiteFlowProperties;
import com.yomahub.liteflowagent.server.service.WxChatService;
import com.yomahub.liteflowagent.server.wx.builder.TextBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MsgHandler extends AbstractHandler {

    @Autowired
    private WxChatService wxChatService;
    @Autowired
    private LiteFlowProperties liteFlowProperties;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context,
                                    WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        if (!wxMessage.getMsgType().equals(WxConsts.XmlMsgType.TEXT)) {
            log.info("收到消息类型不是文本消息");
            return null;
        }

        // 组装回复消息
        String content = null;
        try {
            String msgId = wxChatService.asyncChat(wxMessage.getFromUser(), wxMessage.getContent());
            content ="请点击链接查看 ： " +  liteFlowProperties.getServer() + "?msgId=" + msgId;
        } catch (BaseException ex) {
            content = ex.getMessage();
        } catch (Exception ex) {
            log.error("chat error", ex);
            content = "聊天异常，请稍后再试";
        }
        return new TextBuilder().build(content, wxMessage, weixinService);
    }
}
