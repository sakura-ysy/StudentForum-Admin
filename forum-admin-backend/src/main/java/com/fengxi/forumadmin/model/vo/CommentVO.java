package com.fengxi.forumadmin.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CommentVO {

    private String id;

    private String content;

    private String topicId;

    private String userId;

    private String username;

    private Date createTime;

    private String timeString;

    private String parentId;

    private String postTitle;

    private String replyToId;

    private String replyToUserName;

    private String level; // 级数
}

