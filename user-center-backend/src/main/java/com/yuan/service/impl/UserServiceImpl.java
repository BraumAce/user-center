package com.yuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuan.model.domain.User;
import com.yuan.service.UserService;
import com.yuan.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yuan.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户逻辑实现
 * @author BraumAce
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    // 盐值,将密码进行混淆
    private static final String SALT = "braumace";

    /**
     * 用户注册实现
     * @param userName      用户名
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @return 用户ID
     */
    @Override
    public long userRegister(String userName, String password, String checkPassword) {
        // 1.校验用户的用户名、密码、校验密码，是否符合要求
        // 1.1 非空校验
        if (StringUtils.isAnyBlank(userName, password, checkPassword)) {
            return -1;
        }

        // 1.2 用户名长度不小于4位
        if (userName.length() < 4) {
            return -1;
        }

        // 1.3 密码不小于8位
        if (password.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 1.4 用户名不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // 使用正则表达式进行校验
        Matcher matcher = Pattern.compile(validPattern).matcher(userName);
        if (matcher.find()) {
            return -1;
        }

        // 1.5 密码和校验密码是否相同
        if (!password.equals(checkPassword)) {
            return -1;
        }

        // 1.6 用户名不能重复，查询数据库当中是否存在相同用户名
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", userName);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 2.对密码进行md5盐值加密（密码千万不要直接明文存到数据库中）
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 3.将用户数据插入到数据库
        User user = new User();
        user.setUserName(userName);
        user.setPassword(encryptPassword);
        int result = userMapper.insert(user);
        if (result < 0) {
            return -1;
        }
        return user.getUserId();
    }

    /**
     * 用户登录实现
     * @param userName 用户名
     * @param password 用户密码
     * @param request  请求对象
     * @return 用户信息
     */
    @Override
    public User userLogin(String userName, String password, HttpServletRequest request) {
        // 1.校验用户的用户名、密码、校验密码，是否符合要求
        // 1.1 非空校验
        if (StringUtils.isAnyBlank(userName, password)) {
            return null;
        }

        // 1.2 用户名长度不小于4位
        if (userName.length() < 4) {
            return null;
        }

        // 1.3 密码不小于8位
        if (password.length() < 8) {
            return null;
        }

        // 1.4 用户名不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // 使用正则表达式进行校验
        Matcher matcher = Pattern.compile(validPattern).matcher(userName);
        if (matcher.find()) {
            return null;
        }

        // 2.对密码进行md5盐值加密（密码千万不要直接明文存到数据库中）
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 查询用户是否存在
        // TODO：此处有bug，会将逻辑删除的用户也查找出来
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", userName);
        queryWrapper.eq("password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userName cannot match password");
            return null;
        }

        // 3.用户信息脱敏
        User safetyUser = getSafetyUser(user);

        // 4.用户登录成功
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser 原始用户信息
     * @return 脱敏后用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }

        User safetyUser = new User();
        safetyUser.setUserId(originUser.getUserId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setNickName(originUser.getNickName());
        safetyUser.setAvatar(originUser.getAvatar());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setLastTime(originUser.getLastTime());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setRemark(originUser.getRemark());

        return safetyUser;
    }

}




