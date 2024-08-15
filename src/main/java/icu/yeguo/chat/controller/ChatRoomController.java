package icu.yeguo.chat.controller;

import icu.yeguo.chat.common.Result;
import icu.yeguo.chat.common.ResultUtils;
import icu.yeguo.chat.model.entity.GroupRoom;
import icu.yeguo.chat.model.vo.Combined.MessageANDUserVO;
import icu.yeguo.chat.model.vo.PageResponse;
import icu.yeguo.chat.service.GroupRoomService;
import icu.yeguo.chat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("messages")
    public Result<PageResponse<MessageANDUserVO>> getMessages(@RequestParam("roomId") Long roomId,
                                                              @RequestParam("currentPage") Long currentPage,
                                                              @RequestParam("pageSize") Long pageSize) {
        return ResultUtils.success(messageService.pageQuery(roomId, currentPage, pageSize));
    }

}
