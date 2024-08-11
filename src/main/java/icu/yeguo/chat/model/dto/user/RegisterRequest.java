package icu.yeguo.chat.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class RegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 86796983573273783L;
    private String email;
    private String username;
    private String password;
    private String confirmPwd;

}
