package icu.yeguo.chat.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class AsyncMessageService {

    @Async("asyncTaskExecutor")  // 使用配置的线程池
    public void sendMessageAsync(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            log.error("发送消息时出错: ", e);
        }
    }
}