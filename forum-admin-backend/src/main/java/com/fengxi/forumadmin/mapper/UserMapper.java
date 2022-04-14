package com.fengxi.forumadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengxi.forumadmin.model.entity.Permission;
import com.fengxi.forumadmin.model.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from ums_user")
    List<User> selectUserList();

    /**
     * 获取用户所有权限
     * @param userName
     * @return
     */
    List<Permission> getPermissionByUsername(String userName);
}
