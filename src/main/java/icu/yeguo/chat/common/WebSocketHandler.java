package icu.yeguo.chat.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import icu.yeguo.chat.exception.BusinessException;
import icu.yeguo.chat.model.entity.Message;
import icu.yeguo.chat.model.entity.User;
import icu.yeguo.chat.model.vo.Combined.MessageANDUserVo;
import icu.yeguo.chat.model.vo.CursorResponse;
import icu.yeguo.chat.model.vo.UserVo;
import icu.yeguo.chat.service.MessageService;
import icu.yeguo.chat.service.UserService;
import icu.yeguo.chat.service.impl.AsyncMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    // 线程安全的Map，用于存储活跃的WebSocket连接
    private static final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    private AsyncMessageService asyncMessageService;

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String id = session.getId(); // 获取sessionID
        log.info("WebSocket connection opened with sessionID=" + id);

        // 将session存入map
        sessionMap.put(session.getId(), session);
        log.info("websocket is open");
        // 建立连接查询历史信息 第一页 数据 size=15
        CursorResponse<MessageANDUserVo> cursorResponse = messageService.cursorQuery(1L, 15L, null);
        asyncMessageService.sendMessageAsync(session, JSONUtil.toJsonStr(cursorResponse));
    }

    @Transactional
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("收到一条新消息: " + payload);

        Map<String, Object> jsonObject = JSONUtil.parseObj(payload);

        Long fromUid = jsonObject.get("fromUid") instanceof Integer ?
                (((Integer) jsonObject.get("fromUid"))).longValue() : (Long) jsonObject.get("fromUid");
        Long roomId = jsonObject.get("roomId") instanceof Integer ?
                (((Integer) jsonObject.get("roomId"))).longValue() : (Long) jsonObject.get("roomId");
        String content = (String) jsonObject.get("content");
        // 创建一条新消息
        Message msg1 = new Message(fromUid, content, roomId);

        // 保存消息
        boolean save = messageService.save(msg1);
        if (!save) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR);
        }
        // 根据id查询消息
        Message msg2 = messageService.getById(msg1.getId());
        if (msg2 == null) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR);
        }
        System.out.println("last message:" + msg2);

        // 根据 消息 的 fromUid 查询用户信息
        User user = userService.getById(msg2.getFromUid());
        // 将User转化为安全的UserVO
        UserVo userVO = BeanUtil.copyProperties(user, UserVo.class);
        // 封装联合类 响应
        MessageANDUserVo messageANDUserVO = new MessageANDUserVo(msg2, userVO);

        // 遍历sessionMap，给所有客户端发送消息
        for (WebSocketSession s : sessionMap.values()) {
            asyncMessageService.sendMessageAsync(s, JSONUtil.toJsonStr(messageANDUserVO));
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());
        log.info("websocket is close");
    }
}
