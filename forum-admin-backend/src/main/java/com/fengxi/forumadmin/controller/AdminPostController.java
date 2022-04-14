package com.fengxi.forumadmin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxi.forumadmin.common.annotation.OperLog;
import com.fengxi.forumadmin.common.api.ApiResult;
import com.fengxi.forumadmin.model.dto.CreatePostDTO;
import com.fengxi.forumadmin.model.entity.Post;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.model.vo.PostVO;
import com.fengxi.forumadmin.service.IPostService;
import com.fengxi.forumadmin.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/admin/post")
@SaCheckPermission("admin")
public class AdminPostController {
    @Resource
    private IPostService iPostService;
    @Resource
    private IUserService iUserService;
    /**
     * 返回所有帖子
     * @param tab
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = iPostService.getList(new Page<>(pageNo, pageSize), tab);
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }

    /**
     * 获取文章详情，以id为检索词
     * @param id
     * @return
     */
    @OperLog(operModul = "Post", operType = "get", operDesc = "查看文章")
    @GetMapping("detail")
    public ApiResult<Map<String, Object>> view(@RequestParam("id") String id) {
        Map<String, Object> map = iPostService.viewTopic(id);
        return ApiResult.success(map);
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @OperLog(operModul = "Post", operType = "get", operDesc = "删除文章")
    @RequestMapping(value = "/delete")
    public ApiResult<String> deletePost(@RequestParam("id") String id){
        iPostService.deletePost(id);
        return ApiResult.success(null,"删除成功");
    }

    /**
     * 管理员发布帖子
     * @param username
     * @param dto
     * @return
     */
    @OperLog(operModul = "Post", operType = "post", operDesc = "管理员发帖")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<Post> create(@RequestBody CreatePostDTO dto, @RequestParam("username") String username) {
        // userName 是从Token中提取的发布者用户名，dto是前端传过来的文章对象
        User user = iUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,username));  // 通过用户名获取具体用户
        Post topic = iPostService.createPost(dto, user);
        return ApiResult.success(topic);
    }
}
