package icu.yeguo.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.model.entity.Message;
import icu.yeguo.chat.model.entity.User;
import icu.yeguo.chat.model.vo.Combined.MessageANDUserVO;
import icu.yeguo.chat.model.vo.PageResponse;
import icu.yeguo.chat.model.vo.UserVO;
import icu.yeguo.chat.service.MessageService;
import icu.yeguo.chat.mapper.MessageMapper;
import icu.yeguo.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    public PageResponse<MessageANDUserVO> pageQuery(long roomId, long currentPage, long pageSize) {
        Page<Message> page = new Page<>(currentPage, pageSize);
        page.setOrders(Collections.singletonList(OrderItem.desc("id")));

        // 查询roomId聊天室的消息
        Page<Message> messagePage = super.page(page, new QueryWrapper<Message>().eq("room_id", roomId));
        List<Message> records = messagePage.getRecords();

        if (records.isEmpty()) {
            return new PageResponse<>(
                    0,
                    0,
                    0,
                    0,
                    null);
        }

        // 获取消息中的所有用户ID
        List<Long> fromUidList = records.stream()
                .map(Message::getFromUid)
                .distinct()
                .toList();

        // 查询所有用户并将其转换为UserVO
        Map<Long, UserVO> userVoMap = userService.listByIds(fromUidList).stream()
                .map(this::getUserVO)
                .collect(Collectors.toMap(UserVO::getId, Function.identity()));

        // 把message和userVO组装到一起
        List<MessageANDUserVO> messageANDUserVOS = records.stream()
                .map(message -> new MessageANDUserVO(message, userVoMap.get(message.getFromUid())))
                .toList();

        return new PageResponse<>(
                messagePage.getPages(),
                messagePage.getCurrent(),
                messagePage.getTotal(),
                messagePage.getSize(),
                messageANDUserVOS);
    }

    @Cacheable(value = "users", key = "#user.getId()")
    public UserVO getUserVO(User user) {
        return BeanUtil.toBean(user, UserVO.class);
    }


    @Cacheable(value = "my_cache") //将方法结果缓存到"my_cache"中，key为方法参数的哈希值
    public String getSomeData(String id) throws InterruptedException {
        // 模拟一个耗时操作，比如从数据库中获取数据
        Thread.sleep(1000); //休眠1秒，模拟耗时操作
        return "data for " + id;
    }
}




