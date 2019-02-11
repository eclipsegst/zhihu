package com.zhihu.demo.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author 邓超
 * @description 检查消息事件
 * @create 2018/9/18
 */
public class CheckMessageEvent extends ApplicationEvent {

    private String userId;

    private String sessionId;

    public CheckMessageEvent(Object source, String userId, String sessionId) {
        super(source);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
