/**
 * Created by WebStorm.
 * User: nirongxu
 * Date: 2020/4/20
 * Description: 文件描述
 */
import axios from "../axios"
import userUrls from "../urls/user"

export default {


  // 获取用户信息
  fetchGetUserInfoId (data) {
    return axios.post(userUrls.getUserInfoId, data)
  },
  // 修改密码
  fetchEditPassword (data) {
    return axios.post(userUrls.editPassword, data)
  },

}

// 用户列表
export function fetchUserList (data) {
  return axios.post(userUrls.userList, data)
}

// 删除用户
export function fetchDeleteUser (data) {
  return axios({
    url: userUrls.deleteUser,
    method: 'get',
    params:{
      id:data
    }
    
  })
}

// 添加用户
export function FetchAddUser (data) {
  return axios.post(userUrls.addUser, data)
}

// 修改用户信息
export function fetchEditUser (data) {
  return axios.post(userUrls.editUser, data)
}

// 获取各角色的用户数量
export function fetchUserNumOfRole () {
  return axios.get(userUrls.userNumOfRole)
}

// 查询用户信息
export function fetchGetUserInfo (data) {
  return axios({
    url: userUrls.getUserInfoId,
    method: 'get',
    params:{
      id:data
    }
    
  })
}
