package com.fengxi.forumadmin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxi.forumadmin.model.entity.Tag;

import java.util.List;

public interface ITagService extends IService<Tag> {
    /**
     * 插入标签
     *
     * @param tags
     * @return
     */
    List<Tag> insertTags(List<String> tags);

}