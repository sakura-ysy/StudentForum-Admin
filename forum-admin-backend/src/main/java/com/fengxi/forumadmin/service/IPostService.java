package com.fengxi.forumadmin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxi.forumadmin.model.dto.CreatePostDTO;
import com.fengxi.forumadmin.model.entity.Post;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.model.vo.PostVO;
import javafx.geometry.Pos;

import java.util.Map;

public interface IPostService extends IService<Post> {
    /**
     * 获取已通过的帖子列表
     *
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> getList(Page<PostVO> page, String tab);

    /**
     * 获取全部待审帖子列表
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> getListOfNotAudited(Page<PostVO> page, String tab);

    /**
     * 返回指定帖子详情
     * @param id
     * @return
     */
    Map<String, Object> viewTopic(String id);

    /**
     * 删除帖子
     * @param id
     * @return
     */
    Boolean deletePost(String id);

    /**
     * 发布帖子
     * @param dto
     * @param user
     * @return
     */
    Post createPost(CreatePostDTO dto, User user);

}
