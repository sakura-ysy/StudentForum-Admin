package com.fengxi.forumadmin.model.vo;

import com.fengxi.forumadmin.model.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVO implements Serializable {
    private static final long serialVersionUID = -1874914139913397588L;
    /**
     * 文章ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String alias;
    /**
     * 账号
     */
    private String username;
    /**
     * 标题
     */
    private String title;
    /**
     * 评论统计
     */
    private Integer comments;
    /**
     * 点赞统计
     */
    private Integer praises;
    /**
     * 收藏次數
     */
    private Integer collects;
    /**
     * 置顶
     */
    private Boolean top;
    /**
     * 加精
     */
    private Boolean essence;
    /**
     * 话题关联标签
     */
    private List<Tag> tags;
    /**
     * 浏览量
     */
    private Integer view;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

    private String timeString;  // 创建时间的字符串格式，前端用
}
