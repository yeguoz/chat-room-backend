package icu.yeguo.chat.service;

import icu.yeguo.chat.model.dto.user.LoginRequest;
import icu.yeguo.chat.model.dto.user.RegisterRequest;
import icu.yeguo.chat.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.yeguo.chat.model.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Lenovo
 * &#064;description  针对表【user(用户表)】的数据库操作Service
 * &#064;createDate  2024-08-10 20:57:53
 */
public interface UserService extends IService<User> {

    Integer register(RegisterRequest registerRequest);

    UserVo login(LoginRequest loginRequest, HttpServletRequest req);

    UserVo getCurrentUser(HttpServletRequest req);
}
