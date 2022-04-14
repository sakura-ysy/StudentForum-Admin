package com.fengxi.forumadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxi.forumadmin.mapper.PermissionMapper;
import com.fengxi.forumadmin.mapper.RolePermissionMapper;
import com.fengxi.forumadmin.model.dto.PermissionRoleDTO;
import com.fengxi.forumadmin.model.entity.Permission;
import com.fengxi.forumadmin.model.entity.RolePermission;
import com.fengxi.forumadmin.service.IPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class IPermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {


    @Resource
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 获取角色的所有权限
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> getAllPermOfRole(String roleId) {
        List<Permission> perList = new ArrayList<>();
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));
        for (RolePermission rolePermission : rolePermissions) {
            Permission perm = baseMapper.selectById(rolePermission.getPermId());
            if (!ObjectUtils.isEmpty(perm)){
                perList.add(perm);
            }
        }
        return perList;
    }

    /**
     * 获取所有权限
     * @return
     */
    @Override
    public List<Permission> getAllPerm() {
        List<Permission> list = baseMapper.selectList(new LambdaQueryWrapper<Permission>());
        return list;
    }

    /**
     * 为角色增加权限
     * @param dto
     */
    @Override
    public void addPermForRole(PermissionRoleDTO dto) {
        Permission permission = baseMapper.selectById(dto.getPermId());
        if (!ObjectUtils.isEmpty(permission)){
            RolePermission addRolePerm = RolePermission.builder()
                    .permId(dto.getPermId())
                    .roleId(dto.getRoleId())
                    .build();
            rolePermissionMapper.insert(addRolePerm);
        }
    }

    /**
     * 编辑权限
     * @param perm
     * @return
     */
    @Override
    public Permission editPermInfo(Permission perm) {
        Permission oldPerm = baseMapper.selectById(perm.getId());
        if (ObjectUtils.isEmpty(oldPerm)){
            return null;
        }
        else {
            if (!ObjectUtils.isEmpty(perm.getPermName()) && !perm.getPermName().equals(oldPerm.getPermName())){
                oldPerm.setPermName(perm.getPermName());
            }
            if (!ObjectUtils.isEmpty(perm.getPermTag()) && !perm.getPermTag().equals(oldPerm.getPermTag())){
                oldPerm.setPermTag(perm.getPermTag());
            }
            if (!ObjectUtils.isEmpty(perm.getUrl()) && !perm.getUrl().equals(oldPerm.getUrl())){
                oldPerm.setUrl(perm.getUrl());
            }
            baseMapper.updateById(oldPerm);
        }
        return oldPerm;
    }

    /**
     * 新增权限
     * @param perm
     * @return
     */
    @Override
    public Permission addPerm(Permission perm) {
        Permission addPerm = Permission.builder()
                .permTag(perm.getPermTag())
                .permName(perm.getPermName())
                .url(perm.getUrl())
                .build();
        baseMapper.insert(addPerm);
        return addPerm;
    }
}
