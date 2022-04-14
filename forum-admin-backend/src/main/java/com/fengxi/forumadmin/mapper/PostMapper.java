package com.fengxi.forumadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxi.forumadmin.model.entity.Post;
import com.fengxi.forumadmin.model.vo.PostVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 分页查询已通过帖子列表
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> selectListAndPage(@Param("page") Page<PostVO> page, @Param("tab") String tab);

    /**
     * 分页查询待审帖子列表
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> selectNotAudited(@Param("page") Page<PostVO> page, @Param("tab") String tab);

}
