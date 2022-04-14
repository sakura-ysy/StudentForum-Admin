package com.fengxi.forumadmin.saToken;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengxi.forumadmin.mapper.RoleMapper;
import com.fengxi.forumadmin.mapper.UserMapper;
import com.fengxi.forumadmin.model.entity.Permission;
import com.fengxi.forumadmin.model.entity.Role;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.service.impl.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Autowired
    private IUserServiceImpl iUserService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        List<Permission> userListPermission = userMapper.getPermissionByUsername((String) loginId);
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<>();
        for (Permission permission : userListPermission) {
            list.add(permission.getPermTag());
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        User user = iUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,loginId));
        Role role = roleMapper.selectById(user.getRoleId());
        list.add(role.getRoleName());
        return list;
    }
}