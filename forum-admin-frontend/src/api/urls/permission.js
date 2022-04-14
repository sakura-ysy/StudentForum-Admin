/**
 * Created by Ysy.
 * User: Ysy
 * Date: 2021/8/27
 * Description: 权限管理
 */
 export default {
    // 增加角色
    addRole: "/permissions/addRole",
    // 删除角色
    delRole: "/permissions/delRole",
    // 分配角色权限
    rolePermissions: "/permissions/rolePermissions",

    // 获取角色列表
    getRoleList: "/admin/permission/role/list",
    // 获取所有权限
    getPermList: "/admin/permission/list",
    // 获取角色所有权限
    getPermListOfRole: "/admin/permission/list/role",
    // 为角色添加权限
    addPermForRole:"/admin/permission/relation/add",
    // 编辑权限
    editPermInfo:"/admin/permission/edit",
    // 删除权限
    deletePerm:"/admin/permission/delete",
    // 新增权限
    addPerm: "/admin/permission/add"
  }