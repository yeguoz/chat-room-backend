package icu.yeguo.chat.model.vo.Combined;

import icu.yeguo.chat.model.entity.Message;
import icu.yeguo.chat.model.vo.UserVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MessageANDUserVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 8306385341140544171L;
    private Message message;
    private UserVo userVo;

    public MessageANDUserVo(Message msg2, UserVo userVo) {
        this.message = msg2;
        this.userVo = userVo;
    }
}
