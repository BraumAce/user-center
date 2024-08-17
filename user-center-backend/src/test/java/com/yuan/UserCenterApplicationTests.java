package com.yuan;

import com.yuan.model.domain.User;
import com.yuan.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@MapperScan("com.yuan.mapper")
public class UserCenterApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void testInsertUser() {
        User user = new User();
        user.setUserName("16937");
        user.setNickName("BraumAce");
        user.setPassword("12345678");
        user.setAvatar("https://blog.braumace.cn/ByteLighting/BraumAce.jpg");
        user.setGender(0);
        user.setPhone("12345678900");
        user.setEmail("1693717911@qq.com");
        user.setUserRole(1);

        boolean result = userService.save(user);
        System.out.println("新增用户ID：" + user.getUserId());
        // 断言，判断是否符合预期结果：assertTrue -- 是否返回为true
        Assertions.assertTrue(result);
    }

    /**
     * 测试出错的情况
     */
    @Test
    void testUserRegister() {
        // 测试非空
        String userName = "zhangsan";
        String password = "";
        String checkPassword = "12345678";
        long result = userService.userRegister(userName, password, checkPassword);
        Assertions.assertEquals(-1, result);

        // 测试账户长度小于4
        userName = "zhang";
        result = userService.userRegister(userName, password, checkPassword);
        Assertions.assertEquals(-1, result);

        // 测试密码小于6位
        userName = "zhangsan";
        password = "1234";
        result = userService.userRegister(userName, password, checkPassword);
        Assertions.assertEquals(-1, result);

        // 测试特殊字符
        userName = "zhangsan@";
        password = "12345678";
        result = userService.userRegister(userName, password, checkPassword);
        Assertions.assertEquals(-1, result);

        // 测试密码和校验密码不相同
        userName = "zhangsan";
        checkPassword = "123457899";
        result = userService.userRegister(userName, password, checkPassword);
        Assertions.assertEquals(-1, result);

        // 测试账号不重复
        userName = "zhangsan";
        checkPassword = "12345678";
        result = userService.userRegister(userName, password, checkPassword);
        Assertions.assertEquals(-1, result);

        // 插入数据
        userName = "zhangsan";
        password = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userName, password, checkPassword);
        Assertions.assertEquals(-1, result);
    }

    @Test
    void testAddUser() {
        // 测试注册新用户
        String userName = "lisi";
        String password = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userName, password, checkPassword);
        System.out.println("新增用户ID：" + result);
    }
}
