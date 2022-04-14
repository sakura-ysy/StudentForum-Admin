package com.fengxi.forumadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxi.forumadmin.mapper.*;
import com.fengxi.forumadmin.model.dto.CreatePostDTO;
import com.fengxi.forumadmin.model.entity.*;
import com.fengxi.forumadmin.model.vo.CommentVO;
import com.fengxi.forumadmin.model.vo.PostVO;
import com.fengxi.forumadmin.model.vo.ProfileVO;
import com.fengxi.forumadmin.service.IPostService;
import com.fengxi.forumadmin.service.IPostTagService;
import com.fengxi.forumadmin.service.ITagService;
import com.fengxi.forumadmin.service.IUserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IPostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    @Resource
    private PostCollectMapper postCollectMapper;
    @Resource
    private PostPraiseMapper postPraiseMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private IUserService iUserService;
    @Resource
    private PostTagMapper postTagMapper;
    @Resource
    private ITagService iTagService;
    @Resource
    private IPostTagService iPostTagService;

    /**
     * 分页查询已通过帖子列表
     * @param page
     * @param tab
     * @return
     */
    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 查询话题
        Page<PostVO> iPage = this.baseMapper.selectListAndPage(page, tab);
        iPage.getRecords().forEach(topic ->{
            String format = sdf.format(topic.getCreateTime());
            topic.setTimeString(format);
            User user = iUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getId, topic.getUserId()));
            if (!ObjectUtils.isEmpty(user)){
                topic.setUsername(user.getUsername());
                topic.setAlias(user.getAlias());
            }
            else {
                topic.setUsername("无名测试");
                topic.setAlias("无昵称");
            }

        });
        // 获取点赞、收藏、评论数
        setReactionNum(iPage);
        return iPage;
    }

    /**
     * 获取全部待审帖子列表
     * @param page
     * @param tab
     * @return
     */
    @Override
    public Page<PostVO> getListOfNotAudited(Page<PostVO> page, String tab){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 查询话题
        Page<PostVO> iPage = this.baseMapper.selectNotAudited(page, tab);
        iPage.getRecords().forEach(topic ->{
            String format = sdf.format(topic.getCreateTime());
            topic.setTimeString(format);
            User user = iUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getId, topic.getUserId()));
            if (!ObjectUtils.isEmpty(user)){
                topic.setUsername(user.getUsername());
                topic.setAlias(user.getAlias());
            }
            else {
                topic.setUsername("无名测试");
                topic.setAlias("无昵称");
            }
        });
        return iPage;
    }

    /**
     * 返回指定帖子的详情
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> viewTopic(String id) {
        Map<String, Object> map = new HashMap<>(16);
        Post topic = this.baseMapper.selectById(id);  // topic 为指定的帖子对象
        Assert.notNull(topic, "当前话题不存在,或已被作者删除");
        // 查询话题详情
        topic.setView(topic.getView() + 1);  // view表示帖子的访问次数，访问一次加一一次
        this.baseMapper.updateById(topic);
        // emoji转码
        topic.setContent(EmojiParser.parseToUnicode(topic.getContent()));
        // 点赞、收藏、评论数
        List<PostCollect> collectList = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId,topic.getId()));
        topic.setCollects(collectList.size());
        List<PostPraise> praiseList = postPraiseMapper.selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId,topic.getId()));
        if (ObjectUtils.isEmpty(praiseList))
            topic.setPraises(0);
        else
            topic.setPraises(praiseList.size());
        List<CommentVO> commentVOList = commentMapper.getCommentsByTopicID(topic.getId());
        topic.setComments(commentVOList.size());
        map.put("topic", topic);

        return map;
    }

    /**
     * 管理员删贴
     * @param postId
     * @return
     */
    @Override
    public Boolean deletePost(String postId) {
        baseMapper.deleteById(postId);
        postTagMapper.delete(new LambdaQueryWrapper<PostTag>().eq(PostTag::getTopicId,postId));
        return true;
    }

    /**
     * 设置点赞、评论、收藏数
     * @param iPage
     */
    private void setReactionNum(Page<PostVO> iPage) {
        iPage.getRecords().forEach(topic -> {
                List<PostCollect> collectList = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId,topic.getId()));
                topic.setCollects(collectList.size());
                List<PostPraise> praiseList = postPraiseMapper.selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId,topic.getId()));
                if (ObjectUtils.isEmpty(praiseList))
                    topic.setPraises(0);
                else
                    topic.setPraises(praiseList.size());
                List<CommentVO> commentVOList = commentMapper.getCommentsByTopicID(topic.getId());
                topic.setComments(commentVOList.size());
        });
    }

    /**
     * 管理员发帖
     * @param dto
     * @param user
     * @return
     */
    @Override
    public Post createPost(CreatePostDTO dto, User user) {
        // 查询帖子标题是否已存在
        Post topic1 = this.baseMapper.selectOne(new LambdaQueryWrapper<Post>().eq(Post::getTitle, dto.getTitle()));
        Assert.isNull(topic1, "话题已存在，请修改");

        // 创建帖子对象，封装
        Post topic = Post.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .content(EmojiParser.parseToAliases(dto.getContent()))
                .createTime(new Date())
                .build();
        this.baseMapper.insert(topic);  // 将该对象插入表单

        // 用户积分增加
        int newScore = user.getScore() + 1;
        iUserService.getBaseMapper().updateById(user.setScore(newScore));

        // 标签
        if (!ObjectUtils.isEmpty(dto.getTags())) {
            // 保存标签
            List<Tag> tags = iTagService.insertTags(dto.getTags());
            // 处理标签与话题的关联
            iPostTagService.createTopicTag(topic.getId(), tags);
        }

        return topic;
    }


}
