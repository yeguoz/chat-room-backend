package icu.yeguo.chat.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 单聊
 * @TableName single_room
 */
@TableName(value ="single_room")
@Data
public class SingleRoom implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 聊天室id
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 用户1 id
     */
    @TableField(value = "uid1")
    private Long uid1;

    /**
     * 用户2 id
     */
    @TableField(value = "uid2")
    private Long uid2;

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
     * 0 正常 1 删除
     */
    @TableLogic(value = "is_deleted")
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}