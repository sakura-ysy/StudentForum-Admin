package com.fengxi.forumadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxi.forumadmin.common.api.ApiResult;
import com.fengxi.forumadmin.mapper.CommentMapper;
import com.fengxi.forumadmin.mapper.PostMapper;
import com.fengxi.forumadmin.model.entity.Comment;
import com.fengxi.forumadmin.model.entity.Post;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.model.vo.CommentVO;
import com.fengxi.forumadmin.model.vo.PostVO;
import com.fengxi.forumadmin.service.IAuditService;
import com.fengxi.forumadmin.service.INotifyService;
import com.fengxi.forumadmin.service.IPostService;
import com.fengxi.forumadmin.service.IUserService;
import com.qiniu.storage.Api;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/admin/audit")
public class AdminAuditController {
    @Resource
    private IPostService iPostService;
    @Resource
    private IAuditService iAuditService;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private IUserService iUserService;
    @Resource
    private INotifyService iNotifyService;

    /**
     * 分页返回待审帖子列表
     * @param tab
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/post/list")
    public ApiResult<Page<PostVO>> NotAuditedPostList(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = iPostService.getListOfNotAudited(new Page<>(pageNo, pageSize), tab);
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }

    /**
     * 审核帖子
     * @param postId
     * @param isPass
     * @return
     */
    @RequestMapping(value = "/post",method = RequestMethod.PUT)
    public ApiResult<Boolean> postAudit(@RequestParam(value = "postId") String postId,
                                 @RequestParam(value = "isPass") Boolean isPass){
        Post post = iPostService.getById(postId);
        if(ObjectUtils.isEmpty(post)){
            return ApiResult.failed("文章不存在");
        }
        if (post.getIsAudited()){
            return ApiResult.failed("文章已经审核过");
        }
        Integer status = iAuditService.auditPost(postId, isPass);
        switch (status){
            case 0:
                return  ApiResult.success(false,"未通过");
            case 1:
                return ApiResult.success(true," 审核通过");
            case 2:
                return ApiResult.failed("文章不存在");
            case 3:
                return ApiResult.failed("用户不存在");
            default:
                return ApiResult.failed();
        }

    }

    /**
     * 分页获取待审评论列表（包括一级二级）
     * @param tab
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/comment/list")
    public ApiResult<Page<CommentVO>> NotAuditedComList(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize){
        Page<CommentVO> commentVOPage = commentMapper.selectNotAudited(new Page<>(pageNo, pageSize), tab);
        commentVOPage.getRecords().forEach(commentVO -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            commentVO.setTimeString(sdf.format(commentVO.getCreateTime()));
            User user = iUserService.getById(commentVO.getUserId());
            if(!ObjectUtils.isEmpty(user)){
                commentVO.setUsername(user.getUsername());
            }
            User replyToUser = iUserService.getById(commentVO.getReplyToId());
            if (!ObjectUtils.isEmpty(replyToUser)){
                commentVO.setReplyToUserName(replyToUser.getUsername());
            }
            Post post = iPostService.getById(commentVO.getTopicId());
            if (!ObjectUtils.isEmpty(post)){
                commentVO.setPostTitle(post.getTitle());
            }
            if(ObjectUtils.isEmpty(commentVO.getParentId())){
                commentVO.setLevel("一级");
            }
            else {
                commentVO.setLevel("二级");
            }
        });
        return ApiResult.success(commentVOPage);
    }

    /**
     * 审核评论
     * @param commentId
     * @param isPass
     * @return
     */
    @RequestMapping(value = "/comment",method = RequestMethod.PUT)
    public ApiResult<Boolean> commentAudit(@RequestParam(value = "commentId") String commentId,
                                        @RequestParam(value = "isPass") Boolean isPass){
        Comment comment = commentMapper.selectById(commentId);
        if (ObjectUtils.isEmpty(comment)){
            return ApiResult.failed("评论不存在");
        }
        Integer status = iAuditService.auditComment(commentId, isPass);
        switch (status){
            case 0:
                return ApiResult.success(false,"审核拒绝");
            case 1:{
                if(ObjectUtils.isEmpty(comment.getParentCommentId())){
                    // 新增一级评论通知
                    Post post = iPostService.getById(comment.getTopicId());
                    iNotifyService.createNotify(post.getUserId(),comment.getUserId(),"2", comment.getTopicId());
                }
                else {
                    // 新增二级评论通知
                    Post post = iPostService.getById(comment.getTopicId());
                    Comment parentComment = commentMapper.selectById(comment.getParentCommentId());

                    if (comment.getReplyToId() != null) {
                        iNotifyService.createNotify(comment.getReplyToId(),comment.getUserId(),"4", comment.getTopicId());
                    }
                        iNotifyService.createNotify(parentComment.getUserId(),comment.getUserId(),"2", comment.getTopicId());
                }
                return ApiResult.success(true,"审核通过");
            }
        }
        return ApiResult.failed();
    }

}
