package icu.yeguo.chat.controller;

import cn.hutool.core.bean.BeanUtil;
import icu.yeguo.chat.common.ResponseCode;
import icu.yeguo.chat.common.Result;
import icu.yeguo.chat.common.ResultUtils;
import icu.yeguo.chat.exception.BusinessException;
import icu.yeguo.chat.model.dto.user.LoginRequest;
import icu.yeguo.chat.model.dto.user.RegisterRequest;
import icu.yeguo.chat.model.entity.User;
import icu.yeguo.chat.model.vo.UserVo;
import icu.yeguo.chat.service.MessageService;
import icu.yeguo.chat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static icu.yeguo.chat.constant.User.OFFLINE;
import static icu.yeguo.chat.constant.User.SESSION;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Value("${picgo.path.windows}")
    private String picgoPathWindows;

    @Value("${picgo.path.linux}")
    private String picgoPathLinux;

    /**
     * 注册
     *
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
     *
     * @param: LoginRequest
     */
    @PostMapping("login")
    public Result<UserVo> login(@RequestBody LoginRequest loginRequest, HttpServletRequest req) {
        if (loginRequest == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.login(loginRequest, req));
    }

    /**
     * 查询当前用户
     *
     * @param: HttpServletRequest
     */
    @GetMapping("currentUser")
    public Result<UserVo> getCurrentUser(HttpServletRequest req) {
        return ResultUtils.success(userService.getCurrentUser(req));
    }

    /*
     * 退出登录
     * */
    @PostMapping("logout")
    public Result<Integer> logout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(SESSION);
        if (user == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        // 设置active
        User newUser = userService.getById(user.getId());
        newUser.setActiveStatus(OFFLINE);
        boolean b = userService.updateById(newUser);
        if (!b)
            log.error("退出时修改状态失败");
        session.invalidate();
        return ResultUtils.success(1);
    }

    @Transactional
    @PostMapping("upload")
    public Result<UserVo> uploadAvatar(@RequestPart(value = "image") MultipartFile image, HttpServletRequest req) {
        Path tempFilePath = null;
        try {
            // 获取当前用户
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute(SESSION);

            // 保存上传文件到临时目录
            String tempDir = System.getProperty("java.io.tmpdir");
            tempFilePath = Paths.get(tempDir, image.getOriginalFilename());
            Files.write(tempFilePath, image.getBytes());

            // 执行 PicGo 命令上传图片
            String os = System.getProperty("os.name").toLowerCase();
            String picgoPath;
            if (os.contains("windows")) {
                picgoPath = picgoPathWindows;
            } else {
                picgoPath = picgoPathLinux;
            }
            String command = picgoPath + " upload " + tempFilePath;
            log.info("Executing command: {}", command);

            Process process = Runtime.getRuntime().exec(command);

            // 读取标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line).append("\n");
            }

            // 读取错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorBuilder = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorBuilder.append(line).append("\n");
            }

            process.waitFor();
            log.info("PicGo output: {}", outputBuilder);
            log.error("PicGo error: {}", errorBuilder);

            String imageUrl = null;
            boolean foundSuccess = false;

            // 解析 PicGo 输出
            String output = outputBuilder.toString();
            for (String outputLine : output.split("\n")) {
                if (foundSuccess) {
                    imageUrl = outputLine.trim();
                    break;
                }
                if (outputLine.contains("[PicGo SUCCESS]:")) {
                    foundSuccess = true;
                }
            }

            if (imageUrl != null && imageUrl.contains("https://")) {
                // 更新用户头像 URL，并保存到数据库
                user.setAvatar(imageUrl);
                userService.updateById(user);
                // 更新缓存
                messageService.updateUserCache(user);

                // 返回包含更新后用户信息的 Result
                UserVo updatedUserVo = BeanUtil.toBean(user, UserVo.class);
                return ResultUtils.success(updatedUserVo);
            } else {
                throw new BusinessException(ResponseCode.UPLOAD_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception during file upload: ", e);
            return ResultUtils.error(500, "上传出现异常");
        } finally {
            // 删除临时文件
            try {
                if (tempFilePath != null) {
                    Files.deleteIfExists(tempFilePath);
                }
            } catch (IOException e) {
                log.error("Failed to delete temp file: " + tempFilePath, e);
            }
        }
    }
}
