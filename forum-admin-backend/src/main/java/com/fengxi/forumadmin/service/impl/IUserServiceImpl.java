package com.fengxi.forumadmin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Tuple;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxi.forumadmin.common.api.ApiResult;
import com.fengxi.forumadmin.mapper.*;
import com.fengxi.forumadmin.model.dto.LoginDTO;
import com.fengxi.forumadmin.model.dto.RegisterDTO;
import com.fengxi.forumadmin.model.entity.*;
import com.fengxi.forumadmin.service.IUserService;
import com.fengxi.forumadmin.uitls.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * /user/login
     * @param dto
     * @return
     */
    @Override
    public SaTokenInfo executeLogin(LoginDTO dto) {
        try {
            User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,dto.getUsername()));  // 获取输入的用户名
            if (ObjectUtils.isEmpty(user)){
                return null;
            }
            String encodePwd = MD5Utils.getPwd(dto.getPassword());  // 获取输入的密码并MD5加密
            if(!encodePwd.equals(user.getPassword()))
            {
                return null;
            }

        } catch (Exception e) {
            log.warn("用户不存在or密码验证失败=======>{}", dto.getUsername());
        }
        /**
         * sa-token配置
         */
        // 用户名作为会话id
        StpUtil.login(dto.getUsername(), true);
        // 生成token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        return tokenInfo;
    }

    /**
     * 用户列表
     * @return
     */
    @Override
    public List<User> getUserList() {
        List<User> list =  userMapper.selectUserList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (User user : list) {
            String format = sdf.format(user.getCreateTime());
            user.setTimeString(format);
            switch (user.getRoleId()){
                case 0:
                    user.setRole("管理员");
                    break;
                case 1:
                    user.setRole("学生");
                    break;
                case 2:
                    user.setRole("教师");
                    break;
                case 3:
                    user.setRole("企业");
                    break;
                default:
                    user.setRole("无");
            }
        }
        return list;
    }

    /**
     * 删除用户
     * @param id
     */
    @Override
    public void deleteUser(String id) {
        baseMapper.deleteById(id);
    }

    /**
     * 新增用户
     * @param dto
     * @return
     */
    @Override
    public User executeAdd(RegisterDTO dto) {
        //查询是否有相同用户名的用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getName()).or().eq(User::getEmail, dto.getEmail());  // 账号已存在或邮箱已存在
        User user = baseMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(user)) {
            return null ;
        }
        User addUser = User.builder()
                .username(dto.getName())
                .alias(dto.getName())
                .password(MD5Utils.getPwd(dto.getPass()))
                .email(dto.getEmail())
                .roleId(dto.getRole())
                .mobile(dto.getMobile())
                .bio(dto.getBio())
                .createTime(new Date())
                .status(true)
                .build();
        baseMapper.insert(addUser);

        // 插入对应的角色表
        if (dto.getRole() == 1){
            Student addStudent = Student.builder().userId(addUser.getId()).userName(dto.getName()).build();
            studentMapper.insert(addStudent);
        }
        else if (dto.getRole() == 2){
            Teacher addTeacher = Teacher.builder().userId(addUser.getId()).userName(dto.getName()).build();
            teacherMapper.insert(addTeacher);
        }
        else if (dto.getRole() == 3){
            Company addCompany = Company.builder().userId(addUser.getId()).userName(dto.getName()).build();
            companyMapper.insert(addCompany);
        }

        return addUser;
    }

    /**
     * 编辑用户
     * 用户名、昵称、身份
     * @param user
     * @return
     */
    @Override
    public Boolean editUser(User user) {
        User oldUser = baseMapper.selectById(user.getId());
        User isNameExit = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,user.getUsername()));
        if (!ObjectUtils.isEmpty(isNameExit)){
            return false;
        }
        if (!ObjectUtils.isEmpty(user.getUsername())){
            oldUser.setUsername(user.getUsername());
        }
        if (!ObjectUtils.isEmpty(user.getAlias())){
            oldUser.setAlias(user.getAlias());
        }
        if ((!ObjectUtils.isEmpty(user.getRoleId()))&&(user.getRoleId() != oldUser.getRoleId())){
            //更新角色表中数据
            companyMapper.delete(new LambdaQueryWrapper<Company>().eq(Company::getUserId,user.getId()));
            studentMapper.delete(new LambdaQueryWrapper<Student>().eq(Student::getUserId,user.getId()));
            teacherMapper.delete(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId,user.getId()));
            if (user.getRoleId() == 1){
                Student addStudent = Student.builder().userId(user.getId()).userName(user.getUsername()).build();
                studentMapper.insert(addStudent);
            }
            else if (user.getRoleId() == 2){
                Teacher addTeacher = Teacher.builder().userId(user.getId()).userName(user.getUsername()).build();
                teacherMapper.insert(addTeacher);
            }
            else if (user.getRoleId() == 3){
                Company addCompany = Company.builder().userId(user.getId()).userName(user.getUsername()).build();
                companyMapper.insert(addCompany);
            }

            // 更新 用户-角色 关联
            UserRole addRelation = UserRole.builder()
                    .userId(user.getId())
                    .roleId(String.valueOf(user.getRoleId()))
                    .build();
            userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,user.getId()).eq(UserRole::getRoleId,oldUser.getRoleId()));
            userRoleMapper.insert(addRelation);

            // 用户身份更新
            oldUser.setRoleId(user.getRoleId());
        }

        // 更新用户
        baseMapper.updateById(oldUser);
        return true;
    }


}
