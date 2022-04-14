package com.fengxi.forumadmin.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxi.forumadmin.model.dto.LoginDTO;
import com.fengxi.forumadmin.model.dto.RegisterDTO;
import com.fengxi.forumadmin.model.entity.User;

import java.util.List;

public interface IUserService extends IService<User> {

    /**
     * 管理员登录
     * @param dto
     * @return 生成的JWT的token
     */
    SaTokenInfo executeLogin(LoginDTO dto);

    /**
     * 用户列表
     * @return
     */
    List<User> getUserList();

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(String id);

    /**
     * 新增用户
     *
     * @param dto
     * @return 注册对象
     */
    User executeAdd(RegisterDTO dto);

    /**
     * 编译用户
     * @param user
     * @return
     */
    Boolean editUser(User user);

}
