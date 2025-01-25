package com.yomahub.liteflowagent.server.util;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPlusUtil {

    private static final ThreadPoolExecutor CHAT_THREAD_POOL = ThreadUtil.newExecutor(64, 64);

    public static ThreadPoolExecutor getChatThreadPool(){
        return CHAT_THREAD_POOL;
    }
}
