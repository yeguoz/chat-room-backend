package icu.yeguo.chat.model.vo.Combined;

import icu.yeguo.chat.model.entity.Message;
import icu.yeguo.chat.model.vo.UserVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MessageANDUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8306385341140544171L;
    private Message message;
    private UserVO userVO;

    public MessageANDUserVO(Message msg2, UserVO userVO) {
        this.message = msg2;
        this.userVO = userVO;
    }
}
