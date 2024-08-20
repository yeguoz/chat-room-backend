package icu.yeguo.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.model.entity.Message;
import icu.yeguo.chat.model.entity.User;
import icu.yeguo.chat.model.vo.Combined.MessageANDUserVo;
import icu.yeguo.chat.model.vo.CursorResponse;
import icu.yeguo.chat.model.vo.UserVo;
import icu.yeguo.chat.service.MessageService;
import icu.yeguo.chat.mapper.MessageMapper;
import icu.yeguo.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 * @description 针对表【message(聊天消息)】的数据库操作Service实现
 * @createDate 2024-08-11 16:32:19
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public CursorResponse<MessageANDUserVo> cursorQuery(Long roomId, Long pageSize, Long cursorId) {
        // 构建查询条件
        QueryWrapper<Message> queryWrapper = new QueryWrapper<Message>()
                .eq("room_id", roomId)
                .eq("is_deleted", 0) // 只查询未删除的记录
                .orderByDesc("id");  // 按照ID倒序排序

        // 如果传入了cursorId游标，则添加ID小于cursorId的条件
        if (cursorId != null) {
            queryWrapper
                    .lt("id", cursorId);
        }

        // 查询消息，限制查询的记录数为pageSize
        List<Message> messageList = super
                .list(queryWrapper.last("LIMIT " + pageSize));

        // 如果查询结果为空，返回一个空的CursorResponse
        if (messageList.isEmpty()) {
            return new CursorResponse<>(roomId, null, null, null);
        }

        // 获取消息中的所有用户ID
        List<Long> fromUidList = messageList
                .stream()
                .map(Message::getFromUid)
                .distinct()
                .toList();

        // 查询所有用户并将其转换为UserVo
        Map<Long, UserVo> userVoMap = userService
                .listByIds(fromUidList)
                .stream()
                .map(this::getUserVo)
                .collect(Collectors.toMap(UserVo::getId, Function.identity()));

        // 把message和userVO组装到一起
        List<MessageANDUserVo> records = messageList
                .stream()
                .map(message -> new MessageANDUserVo(message, userVoMap.get(message.getFromUid())))
                .toList();

        // 获取当前页最后一条记录的ID作为新的游标
        Long newCursorId = messageList.get(messageList.size() - 1).getId();
        // 返回CursorResponse对象，包含新的游标ID、记录数和记录列表
        return new CursorResponse<>(roomId, (long) records.size(), newCursorId, records);
    }

    @Cacheable(value = "users", key = "#user.getId()")
    public UserVo getUserVo(User user) {
        return BeanUtil.toBean(user, UserVo.class);
    }

    @CachePut(value = "users", key = "#user.getId()")
    public UserVo updateUserCache(User user) {
        return BeanUtil.toBean(user, UserVo.class);
    }

    @Cacheable(value = "my_cache") //将方法结果缓存到"my_cache"中，key为方法参数的哈希值
    public String getSomeData(String id) throws InterruptedException {
        // 模拟一个耗时操作，比如从数据库中获取数据
        Thread.sleep(1000); //休眠1秒，模拟耗时操作
        return "data for " + id;
    }
}




