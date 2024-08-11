package icu.yeguo.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.model.entity.GroupRoom;
import icu.yeguo.chat.service.GroupRoomService;
import icu.yeguo.chat.mapper.GroupRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【group_room(群聊)】的数据库操作Service实现
* @createDate 2024-08-11 16:32:19
*/
@Service
public class GroupRoomServiceImpl extends ServiceImpl<GroupRoomMapper, GroupRoom>
    implements GroupRoomService{

    @Autowired
    private GroupRoomMapper groupRoomMapper;

    @Transactional
    @Override
    public List<GroupRoom> getGlobalRoomList() {
        LambdaQueryWrapper<GroupRoom> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GroupRoom::getIsGlobalGroup, 1);
        return groupRoomMapper.selectList(lambdaQueryWrapper);
    }
}




