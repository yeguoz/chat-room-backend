package icu.yeguo.chat.controller;

import icu.yeguo.chat.common.Result;
import icu.yeguo.chat.common.ResultUtils;
import icu.yeguo.chat.model.entity.GroupRoom;
import icu.yeguo.chat.model.vo.Combined.MessageANDUserVo;
import icu.yeguo.chat.model.vo.CursorResponse;
import icu.yeguo.chat.model.vo.UserVo;
import icu.yeguo.chat.service.GroupRoomService;
import icu.yeguo.chat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    @Autowired
    private GroupRoomService groupRoomService;
    @Autowired
    private MessageService messageService;

    /**
     * 查询全局房间
     */
    @GetMapping("global")
    public Result<GroupRoom> getGlobalRoom() {
        return ResultUtils.success(groupRoomService.getGlobalRoom());
    }

    /**
     * 查询聊天室消息
     *
     * @return Result<CursorResponse < MessageANDUserVo>>
     * @param: roomId
     * @param: pageSize
     * @param: cursorId
     */
    @GetMapping("messages")
    public Result<CursorResponse<MessageANDUserVo>> getMessages(@RequestParam("roomId") Long roomId,
                                                                @RequestParam("pageSize") Long pageSize,
                                                                @RequestParam(value = "cursorId", required = false)
                                                                Long cursorId) {
        return ResultUtils.success(messageService.cursorQuery(roomId, pageSize, cursorId));
    }

    /**
     * 查询聊天室用户
     */
    @GetMapping("groupUsers")
    public Result<List<UserVo>> getGroupUsers(@RequestParam("roomId") Long roomId) {
        return ResultUtils.success(groupRoomService.getGroupUsers(roomId));
    }
}
