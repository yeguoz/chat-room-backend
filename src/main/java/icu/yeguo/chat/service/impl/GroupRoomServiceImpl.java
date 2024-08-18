package icu.yeguo.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.common.ResponseCode;
import icu.yeguo.chat.exception.BusinessException;
import icu.yeguo.chat.mapper.GroupMemberMapper;
import icu.yeguo.chat.mapper.UserMapper;
import icu.yeguo.chat.model.entity.GroupMember;
import icu.yeguo.chat.model.entity.GroupRoom;
import icu.yeguo.chat.model.entity.User;
import icu.yeguo.chat.model.vo.UserVo;
import icu.yeguo.chat.service.GroupRoomService;
import icu.yeguo.chat.mapper.GroupRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private UserMapper userMapper;

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

    @Transactional
    @Override
    public List<UserVo> getGroupUsers(Long roomId) {
        // 根据roomId 找到groupRoomId 根据groupRoomId 找到members
        GroupRoom groupRoom = groupRoomMapper.selectOne(new LambdaQueryWrapper<GroupRoom>()
                .eq(GroupRoom::getRoomId, roomId));
        if (groupRoom == null)
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);

        List<GroupMember> groupMembers = groupMemberMapper.selectList(new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupRoomId, groupRoom.getId()));
        if (groupMembers == null || groupMembers.isEmpty())
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        // 取出 所有用户uid
        List<Long> Uids = groupMembers.stream().map(GroupMember::getUid).toList();
        // 根据uid 找到用户信息
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                .in(User::getId, Uids));
        if (users == null || users.isEmpty())
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);

        return users.stream()
                .map(user -> BeanUtil.toBean(user, UserVo.class))
                .toList();
    }
}




