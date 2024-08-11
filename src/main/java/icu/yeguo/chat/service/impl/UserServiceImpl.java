package icu.yeguo.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.yeguo.chat.common.ResponseCode;
import icu.yeguo.chat.exception.BusinessException;
import icu.yeguo.chat.mapper.GroupMemberMapper;
import icu.yeguo.chat.model.dto.user.LoginRequest;
import icu.yeguo.chat.model.dto.user.RegisterRequest;
import icu.yeguo.chat.model.entity.GroupMember;
import icu.yeguo.chat.model.entity.User;
import icu.yeguo.chat.model.vo.UserVO;
import icu.yeguo.chat.service.UserService;
import icu.yeguo.chat.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static icu.yeguo.chat.constant.ChatRoom.GLOBAL_GROUP_ID;
import static icu.yeguo.chat.constant.User.*;

/**
 * @author Lenovo
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-08-10 20:57:53
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
    //构建
    private final SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Transactional
    @Override
    public Integer register(RegisterRequest registerRequest) {
        if (BeanUtil.isEmpty(registerRequest)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }

        String email = registerRequest.getEmail();
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String confirmPwd = registerRequest.getConfirmPwd();

        validateRegisterRequest(email, password, confirmPwd);

        // 邮箱是否已经存在
        if (isEmailExists(email)) {
            throw new BusinessException(ResponseCode.MAILBOX_EXIST_ERROR);
        }
        // 用户名是否已经存在
        if (isUsernameExists(username)) {
            throw new BusinessException(ResponseCode.USERNAME_EXIST_ERROR);
        }


        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        // 加密密码
        String encryptedPassword = aes.encryptHex(password);
        user.setPassword(encryptedPassword);

        int userInsert = userMapper.insert(user);
        if (userInsert != 1) {
            throw new BusinessException(ResponseCode.REGISTER_ERROR);
        }

        // 将用户加入全局群中
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupRoomId(GLOBAL_GROUP_ID);
        groupMember.setUid(user.getId());
        int gMemberInsert = groupMemberMapper.insert(groupMember);
        if (gMemberInsert != 1) {
            throw new BusinessException(ResponseCode.REGISTER_ERROR);
        }
        return SUCCESS;
    }

    @Override
    public UserVO login(LoginRequest loginRequest, HttpServletRequest req) {
        if (BeanUtil.isEmpty(loginRequest)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        // 邮箱是否符合规则
        validateEmailFormat(email);
        // 密码长度是否符合规则
        validatePasswordLength(password);
        // 邮箱是否存在
        if (!isEmailExists(email)) {
            throw new BusinessException(ResponseCode.MAILBOX_EXIST_ERROR);
        }
        // 根据邮箱查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // 数据库解密密码和输入密码是否相同
        if (!aes.decryptStr(user.getPassword()).equals(password)) {
            throw new BusinessException(ResponseCode.PASSWD_ERROR);
        }
        // 设置session
        HttpSession session = req.getSession();
        session.setAttribute(SESSION, user);
        // 返回userVO
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public UserVO getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(SESSION);
        if (currentUser == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        // 返回userVO
        return BeanUtil.copyProperties(currentUser, UserVO.class);
    }


    private void validateRegisterRequest(String email, String password, String confirmPwd) {
        // 邮箱规则验证
        validateEmailFormat(email);

        // 密码长度验证
        validatePasswordLength(password);

        // 确认密码验证
        if (!password.equals(confirmPwd)) {
            throw new BusinessException(ResponseCode.PASSWD_DIFFERENT_ERROR);
        }
    }

    private void validateEmailFormat(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BusinessException(ResponseCode.MAILBOX_FORMAT_ERROR);
        }
    }

    private void validatePasswordLength(String password) {
        if (password.length() < 8) {
            throw new BusinessException(ResponseCode.PASSWORD_LENGTH_ERROR);
        }
    }

    private boolean isUsernameExists(String username) {
        return count(new LambdaQueryWrapper<User>().eq(User::getUsername, username)) > 0;
    }

    private boolean isEmailExists(String email) {
        return count(new LambdaQueryWrapper<User>().eq(User::getEmail, email)) > 0;
    }
}


