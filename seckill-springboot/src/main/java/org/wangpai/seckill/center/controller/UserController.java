package org.wangpai.seckill.center.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wangpai.seckill.center.response.RspMsg;
import org.wangpai.seckill.center.service.UserService;
import org.wangpai.seckill.center.session.SessionUtil;
import org.wangpai.seckill.vo.LoginVo;
import org.wangpai.seckill.vo.RegistrationVo;

import static org.wangpai.seckill.center.response.RspState.ERROR;
import static org.wangpai.seckill.center.response.RspState.SUCCESS;

/**
 * @since 2022-2-26
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 此方法不能要求调用前用户必须登录
     *
     * @since 2022-2-26
     */
    @PostMapping("/login")
    @ResponseBody
    @CrossOrigin
    public RspMsg login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginVo loginVo)
            throws JsonProcessingException {
        System.out.println("login called");
        System.out.println("session id：" + SessionUtil.getSessionId(request));

        var loginData = this.userService.getLoginData(request, response, loginVo.getPhone());
        var user = loginData.getUser();
        if (user == null) {
            // 如果通过手机号查找不到 User，说明提供的手机号不正确
            return new RspMsg().setState(ERROR).setMsg("输入的手机号不存在");
        }

        if (!Objects.equals(loginVo.getPassword(), user.getPassword())) {
            return new RspMsg().setState(ERROR).setMsg("输入的密码有误");
        }

        this.userService.resetLoginCache(response, user, loginData.getSessionId());
        return new RspMsg().setState(SUCCESS).setMsg("密码正确");
    }

    /**
     * 此方法不能要求调用前用户必须登录
     *
     * @since 2022-2-26
     */
    @PostMapping("/registration")
    @ResponseBody
    @CrossOrigin
    public RspMsg registration(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody RegistrationVo registrationVo) {
        System.out.println("registration called");
        System.out.println("session id：" + SessionUtil.getSessionId(request));

        try {
            this.userService.addUser(request, response, registrationVo);
        } catch (DuplicateKeyException duplicateKeyException) {
            // FIXME 异常日志

            return new RspMsg().setState(ERROR).setMsg("该手机号已注册");
        } catch (Exception exception) {
            // FIXME 异常日志

            return new RspMsg().setState(ERROR).setMsg("发生了未知错误");
        }

        return new RspMsg().setState(SUCCESS).setMsg("注册成功");
    }
}
