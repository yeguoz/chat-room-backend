package icu.yeguo.chat.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户表
 * @TableName  user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户名 需要唯一
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 0 女 1 男
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 0 下线 1在线
     */
    @TableField(value = "active_status")
    private Integer activeStatus;

    /**
     * 最后上线时间
     */
    @TableField(value = "last_online_time")
    private Date lastOnlineTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 0 未删除 1 删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public User() {

    }
}