package icu.yeguo.chat.controller;

import icu.yeguo.chat.common.ResponseCode;
import icu.yeguo.chat.common.Result;
import icu.yeguo.chat.common.ResultUtils;
import icu.yeguo.chat.exception.BusinessException;
import icu.yeguo.chat.model.dto.user.LoginRequest;
import icu.yeguo.chat.model.dto.user.RegisterRequest;
import icu.yeguo.chat.model.vo.UserVO;
import icu.yeguo.chat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param: RegisterRequest
     */
    @PostMapping("register")
    public Result<Integer> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.register(registerRequest));
    }

    /**
     * 登录
     * @param: LoginRequest
     */
    @PostMapping("login")
    public Result<UserVO>  login(@RequestBody LoginRequest loginRequest, HttpServletRequest req) {
        if (loginRequest == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.login(loginRequest,req));
    }

    /**
     * 查询当前用户
     * @param: HttpServletRequest
     * */
    @GetMapping("currentUser")
    public Result<UserVO> getCurrentUser(HttpServletRequest req) {
        return ResultUtils.success(userService.getCurrentUser(req));
    }

    /*
     * 退出登录
     * */
    @PostMapping("logout")
    public Result<Integer> logout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.invalidate();
        return ResultUtils.success(1);
    }
}
