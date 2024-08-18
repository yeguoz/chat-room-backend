package icu.yeguo.chat.config;

import icu.yeguo.chat.model.entity.User;
import icu.yeguo.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static icu.yeguo.chat.constant.User.OFFLINE;
import static icu.yeguo.chat.constant.User.SESSION;

/**
 * session 事件监听器
 */
@Slf4j
@Component
public class SessionEventListener {

    @Autowired
    private UserService userService;

    /**
     * session过期后 更新用户活跃状态
     * @param: event
     */
    @Transactional
    @EventListener
    public void processSessionExpiredEvent(SessionExpiredEvent event) {
        System.out.println("过期了::sessionID::" + event.getSessionId());
        User user = event.getSession().getAttribute(SESSION);
        if (user == null) {
            return;
        }
        // 更新 active_status
        user.setActiveStatus(OFFLINE);
        boolean b = userService.updateById(user);
        if (!b) {
            log.error("更新用户活跃状态失败");
            return;
        }
        log.info("更新用户活跃状态成功");
    }
}