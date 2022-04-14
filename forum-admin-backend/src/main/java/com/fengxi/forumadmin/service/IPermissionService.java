package com.fengxi.forumadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxi.forumadmin.model.dto.PermissionRoleDTO;
import com.fengxi.forumadmin.model.entity.Permission;

import java.util.List;

public interface IPermissionService extends IService<Permission> {

    /**
     * 获取角色所有权限
     * @param roleId
     * @return
     */
    List<Permission> getAllPermOfRole(String roleId);

    /**
     * 返回所有权限
     * @return
     */
    List<Permission> getAllPerm();

    /**
     * 为角色增加权限
     * @param dto
     */
    void addPermForRole(PermissionRoleDTO dto);

    /**
     * 编辑权限
     * @param perm
     * @return
     */
    Permission editPermInfo(Permission perm);

    /**
     * 新增权限
     * @param perm
     * @return
     */
    Permission addPerm(Permission perm);
}
