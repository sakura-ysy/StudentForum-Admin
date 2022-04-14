package com.fengxi.forumadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxi.forumadmin.model.entity.Comment;
import com.fengxi.forumadmin.model.vo.CommentVO;
import com.fengxi.forumadmin.model.vo.PostVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// 方法定义在 CommentMapper.xml 文件中
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 一级评论
     * @param topicid
     * @return
     */
    List<CommentVO> getCommentsByTopicID(@Param("topicid") String topicid);

    /**
     * 二级评论
     * @param parentId
     * @return
     */
    List<CommentVO> getSecondLevelCommentsByParentID(@Param("topicId") String topicId, @Param("parentId") String parentId);

    /**
     * 分页查询待审评论（包括一级和二级）
     * @param page
     * @param tab
     * @return
     */
    Page<CommentVO> selectNotAudited(@Param("page") Page<PostVO> page, @Param("tab") String tab);
}
