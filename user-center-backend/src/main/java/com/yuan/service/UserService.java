package com.yuan.service;

import com.yuan.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户逻辑接口
 * @author BraumAce
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册接口
     * @param userName      用户名
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @return 用户ID
     */
    long userRegister(String userName, String password, String checkPassword);

    /**
     * 用户登录接口
     * @param userName 用户名
     * @param password 用户密码
     * @param request  请求对象
     * @return 用户信息
     */
    User userLogin(String userName, String password, HttpServletRequest request);

    /**
     * 用户脱敏接口
     * @param originUser 原始用户信息
     * @return 脱敏后用户信息
     */
    User getSafetyUser(User originUser);
}
