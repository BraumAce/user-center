package com.yuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuan.model.domain.User;
import com.yuan.model.domain.request.UserLoginRequest;
import com.yuan.model.domain.request.UserRegisterRequest;
import com.yuan.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yuan.constant.UserConstant.ADMIN_ROLE;
import static com.yuan.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户控制器
 * @author BraumAce
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求
     * @return 用户ID
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }

        String userName = userRegisterRequest.getUserName();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userName, password, checkPassword)) {
            return null;
        }

        return userService.userRegister(userName, password, checkPassword);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求
     * @param request          请求对象
     * @return 用户信息
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }

        String userName = userLoginRequest.getUserName();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(userName, password)) {
            return null;
        }

        return userService.userLogin(userName, password, request);
    }

    /**
     * 用户注销
     * @param request 请求对象
     * @return 结果
     */
    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request) {
        if (request == null) {
            return -1;
        }

        return userService.userLogout(request);
    }

    /**
     * 获取当前用户
     * @param request 请求对象
     * @return 用户信息
     */
    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        // 获取登录态
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return null;
        }
        // 根据id获取到用户信息，去数据库查询
        Long userId = currentUser.getUserId();
        // TODO 校验用户是否合法，比如封号等
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    /**
     * 查询用户
     * @param userName 用户名
     * @param request  请求对象
     * @return 用户列表
     */
    @GetMapping("/search")
    public List<User> searchUsers(String userName, HttpServletRequest request) {
        // 管理员校验
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream()
                .map(userService::getSafetyUser)
                .collect(Collectors.toList());
    }

    /**
     * 删除用户
     * @param userId  用户ID
     * @param request 请求对象
     * @return 结果
     */
    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestBody long userId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;
        }

        if (userId < 0 ) {
            return false;
        }
        return userService.removeById(userId);
    }

    private boolean isAdmin(HttpServletRequest request) {
        // 管理员校验
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
