package icu.yeguo.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.common.ResponseCode;
import icu.yeguo.chat.exception.BusinessException;
import icu.yeguo.chat.model.entity.GroupRoom;
import icu.yeguo.chat.service.GroupRoomService;
import icu.yeguo.chat.mapper.GroupRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static icu.yeguo.chat.constant.ChatRoom.GLOBAL_GROUP_ID;

/**
 * @author Lenovo
 * @description 针对表【group_room(群聊)】的数据库操作Service实现
 * @createDate 2024-08-11 16:32:19
 */
@Service
public class GroupRoomServiceImpl extends ServiceImpl<GroupRoomMapper, GroupRoom>
        implements GroupRoomService {

    @Autowired
    private GroupRoomMapper groupRoomMapper;

    @Override
    public GroupRoom getGlobalRoom() {
        LambdaQueryWrapper<GroupRoom> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GroupRoom::getId, GLOBAL_GROUP_ID);
        GroupRoom groupRoom = groupRoomMapper.selectOne(lambdaQueryWrapper);
        if (groupRoom == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        return groupRoom;
    }
}




