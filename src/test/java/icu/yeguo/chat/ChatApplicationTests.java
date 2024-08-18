package icu.yeguo.chat;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import icu.yeguo.chat.model.vo.Combined.MessageANDUserVo;
import icu.yeguo.chat.model.vo.CursorResponse;
import icu.yeguo.chat.model.vo.UserVo;
import icu.yeguo.chat.service.GroupRoomService;
import icu.yeguo.chat.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;
import java.util.List;


import static icu.yeguo.chat.constant.User.KEY;

@SpringBootTest
class ChatApplicationTests {
    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupRoomService groupRoomService;

    @Test
    public void getGroupMember() {
        List<UserVo> groupUsers = groupRoomService.getGroupUsers(1L);
        groupUsers.forEach(System.out::println);
    }

    @Test
    public void cursorQueryTest() {
        CursorResponse<MessageANDUserVo> result = messageService
                .cursorQuery(1L, 5L, null);
        CursorResponse<MessageANDUserVo> result1 = messageService
                .cursorQuery(1L, 5L, result.getCursorId());
        System.out.println(result);
        System.out.println(result1);

    }

    @Test
    public void cacheTest() {
        StopWatch stopWatch = new StopWatch("getData");
        stopWatch.start("1");
        System.out.println(messageService.cursorQuery(1L, 20L, null));
        stopWatch.stop();
        stopWatch.start("2");
        System.out.println(messageService.cursorQuery(1L, 20L, 150L));
        stopWatch.stop();
        stopWatch.start("3");
        System.out.println(messageService.cursorQuery(1L, 20L, 20L));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    void contextLoads() {
        byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        String s = aes.encryptHex("123456");
        String en = aes.decryptStr(s, CharsetUtil.CHARSET_UTF_8);
        System.out.println(s);
        System.out.println(en);
    }

}
