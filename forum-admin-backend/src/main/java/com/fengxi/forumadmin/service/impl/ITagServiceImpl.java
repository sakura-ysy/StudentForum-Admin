package com.fengxi.forumadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxi.forumadmin.mapper.TagMapper;
import com.fengxi.forumadmin.model.entity.Tag;
import com.fengxi.forumadmin.service.IPostService;
import com.fengxi.forumadmin.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ITagServiceImpl  extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Resource
    private com.fengxi.forumadmin.service.IPostService IPostService;

    @Override
    public List<Tag> insertTags(List<String> tagNames) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            // 判断该标签是否已存在
            Tag tag = this.baseMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tagName));
            if (tag == null) {
                // 不存在，则创建一个并插入数据库
                tag = Tag.builder().name(tagName).build();
                this.baseMapper.insert(tag);
            } else {
                // 存在，将该标签下的帖子数加一
                tag.setTopicCount(tag.getTopicCount() + 1);
                this.baseMapper.updateById(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }
}
