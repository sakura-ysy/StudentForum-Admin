package com.fengxi.forumadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;

public interface IAuditService {

    /**
     * 审核帖子
     * @param postId
     * @param isPass
     * @return
     */
    Integer auditPost(String postId, Boolean isPass);

    /**
     * 审核评论
     * @param commentId
     * @param isPass
     * @return
     */
    Integer auditComment(String commentId, Boolean isPass);
}
