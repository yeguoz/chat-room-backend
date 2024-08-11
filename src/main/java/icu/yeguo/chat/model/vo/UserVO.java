package icu.yeguo.chat.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7129999255892623821L;
    private Long id;
    private String email;
    private String username;
    private String avatar;
    private Integer gender;
    private Integer activeStatus;
    private Date lastOnlineTime;
    private Date createTime;
    private Date updateTime;
}
