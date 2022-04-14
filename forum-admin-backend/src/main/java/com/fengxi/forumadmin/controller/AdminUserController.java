package com.fengxi.forumadmin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengxi.forumadmin.common.annotation.OperLog;
import com.fengxi.forumadmin.common.api.ApiResult;
import com.fengxi.forumadmin.mapper.UserMapper;
import com.fengxi.forumadmin.mapper.UserRoleMapper;
import com.fengxi.forumadmin.model.dto.LoginDTO;
import com.fengxi.forumadmin.model.dto.RegisterDTO;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.model.entity.UserRole;
import com.fengxi.forumadmin.service.IUserService;
import com.qiniu.storage.Api;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Resource
    private IUserService iUserService;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private AdminUserController adminUserController;

    /**
     * 登录
     * @param dto
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        User user = iUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
        .eq(User::getUsername,dto.getUsername()));
        SaTokenInfo tokenInfo = iUserService.executeLogin(dto);
        if (ObjectUtils.isEmpty(tokenInfo)) {
            return ApiResult.failed("账号密码错误");
        }
        System.out.println(tokenInfo);
        return adminUserController.loginSuccess(tokenInfo,user);
    }

    /**
     * 登录成功的log日志
     */
    @OperLog(operModul = "User",operType = "post", operDesc = "管理员登录")
    public ApiResult<Map<String,String>> loginSuccess(SaTokenInfo tokenInfo,User user){
        Map<String, String> map = new HashMap<>(16);
        map.put("token", tokenInfo.getTokenValue());
        map.put("roleId", String.valueOf(user.getRoleId()));
        return ApiResult.success(map, "登录成功");
    }


    /**
     * 用户列表
     * @return
     */
    @SaCheckPermission("admin")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ApiResult<List<User>> getUserList(){
        List<User> list = iUserService.getUserList();
        return  ApiResult.success(list);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @OperLog(operModul = "User", operType = "get", operDesc = "管理员删除用户")
    @SaCheckPermission("admin")
    @RequestMapping(value = "/delete")
    public ApiResult<String> deleteUser(@RequestParam("id") String id){
        iUserService.deleteUser(id);
        return ApiResult.success(null,"删除成功");
    }

    /**
     * 通过id查用户
     * @param id
     * @return
     */
    @OperLog(operModul = "User", operType = "get", operDesc = "管理员查询用户")
    @SaCheckPermission("admin")
    @RequestMapping("/info")
    public ApiResult<User> getUserInfo(@RequestParam("id") String id){
        User user = iUserService.getBaseMapper().selectById(id);
        if (ObjectUtils.isEmpty(user)){
            return ApiResult.failed("用户不存在");
        }
        return ApiResult.success(user,"查询成功");
    }


    /**
     * 新增用户
     * @param dto
     * @return
     */
    @OperLog(operModul = "User", operType = "post", operDesc = "管理员新增用户")
    @SaCheckPermission("admin")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        User user = iUserService.executeAdd(dto);
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("账号或邮箱已存在！");
        }
        // 加入用户-角色关联表
        UserRole addRelation = UserRole.builder()
                .userId(user.getId())
                .roleId(String.valueOf(dto.getRole()))
                .build();
        userRoleMapper.insert(addRelation);
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }

    /**
     * 管理员编辑用户
     * 用户名、昵称、身份
     * @param user
     * @return
     */
    @OperLog(operModul = "User", operType = "post", operDesc = "管理员编辑用户")
    @SaCheckPermission("admin")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ApiResult<Object> editUser(@Valid @RequestBody User user){
        System.out.println(user);
        Boolean b = iUserService.editUser(user);
        if (!b){
            return ApiResult.failed("用户名已存在");
        }
        return ApiResult.success(null,"修改成功") ;
    }

    /**
     * 获取各角色用户数量
     * @return
     */
    @SaCheckPermission("admin")
    @RequestMapping(value = "/numOfRole",method = RequestMethod.GET)
    public ApiResult<Map<String,Integer>> getUserNumOfRole(){
        List<User> adminList = iUserService.getBaseMapper().selectList(new LambdaQueryWrapper<User>().eq(User::getRoleId, 0));
        List<User> studentList = iUserService.getBaseMapper().selectList(new LambdaQueryWrapper<User>().eq(User::getRoleId, 1));
        List<User> teacherList = iUserService.getBaseMapper().selectList(new LambdaQueryWrapper<User>().eq(User::getRoleId, 2));
        List<User> companyList = iUserService.getBaseMapper().selectList(new LambdaQueryWrapper<User>().eq(User::getRoleId, 3));
        Map<String,Integer> map = new HashMap<>();
        map.put("adminNum",adminList.size());
        map.put("studentNum",studentList.size());
        map.put("teacherNum",teacherList.size());
        map.put("companyNum",companyList.size());
        return ApiResult.success(map);
    }

}
