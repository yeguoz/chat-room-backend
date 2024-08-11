package icu.yeguo.chat;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static icu.yeguo.chat.constant.UserConstant.KEY;

class ChatApplicationTests {

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
