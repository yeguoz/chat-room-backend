package icu.yeguo.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.model.entity.Message;
import icu.yeguo.chat.service.MessageService;
import icu.yeguo.chat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【message(聊天消息)】的数据库操作Service实现
* @createDate 2024-08-11 16:32:19
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




