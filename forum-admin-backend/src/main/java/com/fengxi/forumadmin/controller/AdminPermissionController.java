package com.fengxi.forumadmin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengxi.forumadmin.common.annotation.OperLog;
import com.fengxi.forumadmin.common.api.ApiResult;
import com.fengxi.forumadmin.mapper.PermissionMapper;
import com.fengxi.forumadmin.mapper.RoleMapper;
import com.fengxi.forumadmin.model.dto.PermissionRoleDTO;
import com.fengxi.forumadmin.model.entity.Permission;
import com.fengxi.forumadmin.model.entity.Role;
import com.fengxi.forumadmin.service.IPermissionService;
import com.fengxi.forumadmin.service.IUserService;
import com.qiniu.storage.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/permission")
@SaCheckPermission("admin")
public class AdminPermissionController {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private IPermissionService iPermissionService;
    @Resource
    private IUserService iUserService;

    /**
     * 获取所有角色
     * @return
     */
    @RequestMapping("/role/list")
    public ApiResult<List<Role>> getAllRole(){
        List<Role> list = roleMapper.selectList(new LambdaQueryWrapper<Role>());
        return ApiResult.success(list);
    }

    /**
     * 获取角色的所有权限
     * @param roleId
     * @return
     */
    @RequestMapping("/list/role")
    public ApiResult<List<Permission>> getAllPermOfRole(@RequestParam("roleId") String roleId){
        List<Permission> allPermOfRole = iPermissionService.getAllPermOfRole(roleId);
        return ApiResult.success(allPermOfRole);
    }

    /**
     * 获取所有权限
     * @return
     */
    @RequestMapping("/list")
    public ApiResult<List<Permission>> getAllPermission(){
        List<Permission> allPerm = iPermissionService.getAllPerm();
        return ApiResult.success(allPerm);
    }

    /**
     * 为角色增加权限
     * @param dto
     * @return
     */
    @OperLog(operModul = "Permission", operType = "post", operDesc = "管理员为角色添加权限")
    @RequestMapping(value = "/relation/add",method = RequestMethod.POST)
    public ApiResult<Object> addPermForRole(@RequestBody PermissionRoleDTO dto){
        iPermissionService.addPermForRole(dto);
        return ApiResult.success();
    }


    /**
     *  管理员编辑权限
     * @param perm
     * @return
     */
    @OperLog(operModul = "Permission", operType = "post", operDesc = "管理员编辑权限")
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ApiResult<Permission> editPermInfo(@RequestBody Permission perm ){
        Permission permission = iPermissionService.editPermInfo(perm);
        if (ObjectUtils.isEmpty(permission)){
            return ApiResult.failed("权限已不存在");
        }
        return ApiResult.success(permission);
    }

    /**
     * 管理员删除权限
     * @param permId
     * @return
     */
    @OperLog(operModul = "Permission", operType = "get", operDesc = "管理员删除权限")
    @RequestMapping(value = "/delete")
    public ApiResult<Void> deletePerm(@RequestParam("permId") String permId ){
        iPermissionService.getBaseMapper().deleteById(permId);
        return ApiResult.success();
    }

    /**
     * 管理员新增权限
     * @param perm
     * @return
     */
    @OperLog(operModul = "Permission", operType = "post", operDesc = "管理员新增权限")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult<Permission> addPerm(@RequestBody Permission perm){
        Permission permission = iPermissionService.getBaseMapper().selectOne(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getPermName, perm.getPermName())
                .or().eq(Permission::getPermTag, perm.getPermTag()));
        if (!ObjectUtils.isEmpty(permission)){
            return ApiResult.failed("权限名或Tag已存在");
        }
        Permission newPerm = iPermissionService.addPerm(perm);
        return ApiResult.success(newPerm,"新增权限成功");
    }
}
