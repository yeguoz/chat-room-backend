package icu.yeguo.chat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 群聊
 * @TableName group_room
 */
@TableName(value ="group_room")
@Data
public class GroupRoom implements Serializable {
    /**
     * 群聊 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房间 id
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 群聊名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 0不是 1是
     */
    @TableField(value = "is_global_group")
    private Integer isGlobalGroup;

    /**
     * 群聊头像
     */
    @TableField(value = "avatar")
    private String avatar;

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
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}