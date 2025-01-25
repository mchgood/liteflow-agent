package com.yomahub.liteflowagent.server.api.impl;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
import com.yomahub.liteflowagent.server.api.ZhiPuApi;
import com.yomahub.liteflowagent.server.properties.ZhiPuProperties;
import com.yomahub.liteflowagent.server.util.JsonUtils;
import com.zhipu.oapi.ClientV4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ZhiPuApiImpl implements ZhiPuApi {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ZhiPuProperties zhipuProperties;
    private static ClientV4 client;

    @Autowired
    public ZhiPuApiImpl(ZhiPuProperties zhipuProperties) {
        this.zhipuProperties = zhipuProperties;
        client = new ClientV4.Builder(URL_PREFIX, zhipuProperties.getApiKey())
                .enableTokenCache()
                .networkConfig(300, 100, 100, 100, TimeUnit.SECONDS)
                .connectionPool(new okhttp3.ConnectionPool(8, 1, TimeUnit.SECONDS))
                .build();
    }

    private static final String URL_PREFIX = "https://open.bigmodel.cn/api/llm-application/open/";
    private static final String CREATE_CONVERSATION_URL = "/v2/application/%s/conversation";
    private static final String CREATE_CHAT_URL = "/v3/application/invoke";
    private static final String GET_CHAT_RESULT_URL = "/v2/model-api/%s/sse-invoke";
    private static final int OUT_TIME_SECONDS = 30;

    @Override
    public String createConversation() {

        String response = HttpRequest.post(URL_PREFIX + String.format(CREATE_CONVERSATION_URL, zhipuProperties.getAppId()))
                .bearerAuth(zhipuProperties.getApiKey())
                .contentType("application/json")
                .execute()
                .body();

        JSONObject jsonObject = JsonUtils.toBean(response, JSONObject.class);

        Optional.ofNullable(jsonObject)
                .map(t -> t.get("code"))
                .map(Object::toString)
                .map(NumberUtil::parseInt)
                .filter(t -> t == 200)
                .orElseThrow(() -> new RuntimeException("create conversation failed response: " + JsonUtils.toJsonString(response)));

        return jsonObject.getJSONObject("data").getString("conversation_id");
    }

    @Override
    public String createChat(String traceId, String conversationId, String content) {
        Map<Object, Object> param = MapBuilder.create(new HashMap<>())
                .put("conversation_id", conversationId)
                .put("app_id", zhipuProperties.getAppId())
                .put("third_request_id", traceId)
                .put("stream",false)
                .put("messages", List.of(
                        MapBuilder.create(new HashMap<>())
                                .put("role", "user")
                                .put("content",List.of(
                                        MapBuilder.create(new HashMap<>())
                                                .put("type", "input")
                                                .put("value", content)
                                                .put("key","query")
                                                .build()
                                ))
                                .build()
                ))
                .build();

        String response = HttpRequest.post(URL_PREFIX + CREATE_CHAT_URL)
                .bearerAuth(zhipuProperties.getApiKey())
                .body(JsonUtils.toJsonString(param))
                .contentType("application/json")
                .execute()
                .body();

        JSONObject jsonObject = JsonUtils.toBean(response, JSONObject.class);
        log.info("chat response {}", jsonObject.toJSONString());
        String result = Optional.ofNullable(jsonObject)
                .map(t -> t.getJSONArray("choices"))
                .map(t->t.getJSONObject(0))
                .map(t->t.getJSONObject("messages"))
                .map(t->t.getJSONObject("content"))
                .map(t->t.getString("msg"))
                .orElseThrow(() -> new RuntimeException("create chat failed response: " + JsonUtils.toJsonString(response)));

        return result;
    }

}
