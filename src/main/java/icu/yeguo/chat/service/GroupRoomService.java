package icu.yeguo.chat.service;

import icu.yeguo.chat.model.entity.GroupRoom;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.yeguo.chat.model.vo.UserVo;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【group_room(群聊)】的数据库操作Service
* @createDate 2024-08-11 16:32:19
*/
public interface GroupRoomService extends IService<GroupRoom> {
    GroupRoom getGlobalRoom();

    List<UserVo> getGroupUsers(Long roomId);
}
