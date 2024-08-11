package icu.yeguo.chat.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class LoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6225348510343799150L;
    private String email;
    private String password;
}
