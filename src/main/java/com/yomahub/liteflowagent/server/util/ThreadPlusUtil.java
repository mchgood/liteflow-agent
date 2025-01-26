package com.yomahub.liteflowagent.server.util;

import cn.hutool.core.thread.BlockPolicy;
import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPlusUtil {

    private static final ThreadPoolExecutor CHAT_THREAD_POOL = new ThreadPoolExecutor(
            64,
            64,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(128),
            ThreadUtil.newNamedThreadFactory("chat-thread-pool", true),
            new BlockPolicy()
    );

    public static ThreadPoolExecutor getChatThreadPool() {
        return CHAT_THREAD_POOL;
    }
}
