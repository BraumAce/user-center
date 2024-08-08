package com.yuan.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体参数
 * @author BraumAce
 */
@Data
public class UserLoginRequest implements Serializable {

//    public static final String USER_LOGIN_STATE = "登录成功";

    // 序列化
    private static final long serialVersionUID = 3553317334228624372L;

    // 用户名
    private String userName;

    // 用户密码
    private String password;

}
