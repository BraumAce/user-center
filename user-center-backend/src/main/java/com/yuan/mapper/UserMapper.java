package com.yuan.mapper;

import com.yuan.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Configuration;

/**
* @author BraumAce
* @description 针对表【user(用户表)】的数据库操作Mapper
* @Entity com.yuan.model.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




