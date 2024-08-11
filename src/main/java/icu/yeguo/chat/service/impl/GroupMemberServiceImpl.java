package icu.yeguo.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.model.entity.GroupMember;
import icu.yeguo.chat.service.GroupMemberService;
import icu.yeguo.chat.mapper.GroupMemberMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【group_member(群聊成员)】的数据库操作Service实现
* @createDate 2024-08-11 16:32:19
*/
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService {

}




