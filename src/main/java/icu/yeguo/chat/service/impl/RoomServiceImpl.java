package icu.yeguo.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.model.entity.Room;
import icu.yeguo.chat.service.RoomService;
import icu.yeguo.chat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【room(聊天室)】的数据库操作Service实现
* @createDate 2024-08-11 16:32:19
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService{

}




