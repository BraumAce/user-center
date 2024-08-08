package com.yuan.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户实体
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "userId", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "userName")
    private String userName;

    /**
     * 用户昵称
     */
    @TableField(value = "nickName")
    private String nickName;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 性别（0 - 男 1 - 女）
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 账号状态（0 - 正常 1 - 停用）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否删除（0 - 正常 1 - 删除）
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 上次登录时间
     */
    @TableField(value = "lastTime")
    private Date lastTime;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 用户角色（0 - 普通用户 1 - 管理员）
     */
    @TableField(value = "userRole")
    private Integer userRole;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}