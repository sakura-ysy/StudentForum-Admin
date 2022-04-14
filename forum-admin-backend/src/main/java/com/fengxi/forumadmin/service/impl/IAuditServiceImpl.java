package com.fengxi.forumadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengxi.forumadmin.mapper.CommentMapper;
import com.fengxi.forumadmin.model.entity.Comment;
import com.fengxi.forumadmin.model.entity.Post;
import com.fengxi.forumadmin.model.entity.Tag;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class IAuditServiceImpl implements IAuditService {
    @Resource
    private IPostService iPostService;
    @Resource
    private IUserService iUserService;
    @Resource
    private ITagService iTagService;
    @Resource
    private IPostTagService iPostTagService;
    @Resource
    private CommentMapper commentMapper;

    /**
     * 审核帖子
     * @param postId
     * @return
     */
    @Override
    public Integer auditPost(String postId, Boolean isPass) {
        Post post = iPostService.getBaseMapper().selectById(postId);
        if (ObjectUtils.isEmpty(post)){
            return 2;
        }
        User user = iUserService.getBaseMapper().selectById(post.getUserId());
        if (ObjectUtils.isEmpty(user)){
            return 3;
        }

        if (isPass){
            // 文章审核通过
            post.setIsAudited(true);
            post.setIsPass(true);
            iPostService.getBaseMapper().updateById(post);

            // 用户积分增加
            int newScore = user.getScore() + 1;
            iUserService.getBaseMapper().updateById(user.setScore(newScore));

            // 标签
            String tags = post.getTags();
            List<Tag> tagList = new ArrayList<>();
            if (ObjectUtils.isEmpty(tags)){
                return 1;
            }
            while(tags.contains(";")){
                int i = tags.indexOf(";");
                String tagName = tags.substring(0,i);
                // 判断该标签是否已存在
                Tag tag = iTagService.getBaseMapper().selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tagName));
                if (tag == null) {
                    // 不存在，则创建一个并插入数据库
                    tag = Tag.builder().name(tagName).build();
                    iTagService.getBaseMapper().insert(tag);
                } else {
                    // 存在，将该标签下的帖子数加一
                    tag.setTopicCount(tag.getTopicCount() + 1);
                    iTagService.getBaseMapper().updateById(tag);
                }
                tagList.add(tag);
                tags = tags.substring(i+1);
            }

            // 处理标签与话题的关联
            iPostTagService.createTopicTag(postId, tagList);

            return 1;
        }
        else {
            // 文章审核不通过
            post.setIsAudited(true);
            post.setIsPass(false);
            iPostService.getBaseMapper().updateById(post);
            return 0;
        }
    }

    /**
     * 审核评论
     * @param commentId
     * @param isPass
     * @return
     */
    @Override
    public Integer auditComment(String commentId, Boolean isPass) {
        Comment comment = commentMapper.selectById(commentId);
        if (isPass){
            comment.setIsAudited(true);
            comment.setIsPass(true);
            commentMapper.updateById(comment);
            return 1;
        }
        else {
            comment.setIsAudited(true);
            comment.setIsPass(false);
            commentMapper.updateById(comment);
            return 0;
        }
    }
}
