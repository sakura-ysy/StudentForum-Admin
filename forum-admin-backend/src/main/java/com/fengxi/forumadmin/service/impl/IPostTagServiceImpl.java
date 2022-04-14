package com.fengxi.forumadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxi.forumadmin.mapper.PostTagMapper;
import com.fengxi.forumadmin.model.entity.PostTag;
import com.fengxi.forumadmin.model.entity.Tag;
import com.fengxi.forumadmin.service.IPostTagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements IPostTagService {
    @Override
    public void createTopicTag(String id, List<Tag> tags) {
        // 先删除topicId对应的所有记录
        this.baseMapper.delete(new LambdaQueryWrapper<PostTag>().eq(PostTag::getTopicId, id));

        // 循环保存对应关联
        tags.forEach(tag -> {
            PostTag topicTag = new PostTag();
            topicTag.setTopicId(id);
            topicTag.setTagId(tag.getId());
            this.baseMapper.insert(topicTag);
        });
    }
}
