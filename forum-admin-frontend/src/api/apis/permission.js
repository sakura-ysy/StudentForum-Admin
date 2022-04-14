/**
 * Created by Ysy.
 * User: Ysy
 * Date: 2021/8/27
 * Description: 权限管理
 */
 import axios from "../axios"
 import permissionUrls from "../urls/permission"
 
 // 角色列表
 export function fetchRoleList () {
   return axios.get(permissionUrls.getRoleList)
 }

 // 权限列表
 export function fetchPermList() {
     return axios.get(permissionUrls.getPermList)
 }

 // 获取角色的所有权限
 export function fetchPermListOfRole(id){
     return axios.request(({
         url:permissionUrls.getPermListOfRole,
         method:'get',
         params:{
             roleId:id
         }
     }))
 }

 // 为角色添加权限
 export function fetchAddPermForRole(data){
    return axios.request(({
      url:permissionUrls.addPermForRole,
      method:'post',
      data:data
    }))
  }

 // 编辑权限
 export function fetchEditPerm(data){
  return axios.request(({
    url:permissionUrls.editPermInfo,
    method:'post',
    data:data
  }))
}

  // 删除权限
  export function fetchDelPerm(id){
    return axios.request(({
      url:permissionUrls.deletePerm,
      method:'get',
      params:{
        permId:id
      }
    }))
  }

  // 新增权限
  export function fetchAddPerm(data){
    return axios.request(({
      url:permissionUrls.addPerm,
      method:'post',
      data:data
    }))
  }


 