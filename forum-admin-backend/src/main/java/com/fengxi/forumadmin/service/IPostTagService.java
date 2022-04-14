package com.fengxi.forumadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxi.forumadmin.model.entity.PostTag;
import com.fengxi.forumadmin.model.entity.Tag;

import java.util.List;

public interface IPostTagService extends IService<PostTag> {
    /**
     * 创建中间关系
     *
     * @param id
     * @param tags
     * @return
     */
    void createTopicTag(String id, List<Tag> tags);
}
