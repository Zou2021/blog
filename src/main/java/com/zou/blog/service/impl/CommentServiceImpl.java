package com.zou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zou.blog.email.EmailService;
import com.zou.blog.email.model.EmailDto;
import com.zou.blog.mapper.custom.CommentsMapper;
import com.zou.blog.mapper.generator.ArticleMapper;
import com.zou.blog.model.domain.Article;
import com.zou.blog.model.domain.Comments;
import com.zou.blog.model.enums.CommentStatus;
import com.zou.blog.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 邹祥发
 * @date: 2022/7/12 08:10
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentsMapper commentsMapper;
    @Resource
    private EmailService emailService;
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void save(Comments comments, int parentCommentId) {
        commentsMapper.insert(comments);
        //将回复的父级id给到
        if (parentCommentId == 0) {
            parentCommentId = comments.getId();
        }
        Article article = articleMapper.selectById(comments.getBlogId());
        //通过父id找到email
        Comments parentComment = commentsMapper.selectById(parentCommentId);
        String email;
        if (comments.getParentId() == -1) {
            //为首节点评论时发送邮件到博主邮箱
            email = "1565453341@qq.com";
        } else {
            email = parentComment.getEmail();
        }
        // 发送邮箱
        this.sendEmail(comments, article, email);
    }

    private void sendEmail(Comments comment, Article article, String email) {
        EmailDto emailDto = new EmailDto();
        emailDto.setBlogTitle(article.getArticleTitle());
        emailDto.setEmailTo(email);
        // 邮件相同不发送
        if (comment.getEmail().equals(email)) {
            return;
        }
        String basePath = "https://www.hqxiaozou.top";
        if (article.getId().equals(1)) {
            basePath += "/about";
        } else {
            basePath += "/post/" + article.getArticleUrl();
        }
        emailDto.setUrl(basePath);
        emailDto.setContent(comment.getContent());
        emailService.sendSimpleEmail(emailDto);
    }

    @Override
    public List<Comments> viewIsNotVisibleComments() {
        List<Comments> comments = commentsMapper.selectList(new QueryWrapper<Comments>().orderByAsc("is_visible").orderByDesc("create_time"));
        return new ArrayList<>(comments);
    }

    @Override
    public void remove(int id) {
        commentsMapper.deleteById(id);
    }

    @Override
    public long commentCount(int id) {
        return commentsMapper.selectCount(new QueryWrapper<Comments>().eq("blog_id", id).eq("is_visible", CommentStatus.VISIBLE.getStatus()));
    }

    @Override
    public List<Comments> listCommentByBlogId(int blogId) {
        QueryWrapper<Comments> wrapper = new QueryWrapper<Comments>().eq("blog_id", blogId).eq("is_visible", CommentStatus.VISIBLE.getStatus()).orderByAsc("sort").orderByDesc("create_time");
        wrapper.select("id", "nickname", "content", "create_time", "avatar", "sort", "parent_id", "province", "parent_name");
        List<Comments> comments = commentsMapper.selectList(wrapper);
        return firstComment(comments);
    }

    public List<Comments> firstComment(List<Comments> comments) {
        //存储父评论为根评论-1的评论
        List<Comments> list = new ArrayList<>();
        for (Comments comment : comments) {
            //其父id小于0则为第一级别的评论
            if (comment.getParentId() == -1) {
                //我们将该评论下的所有评论都查出来
                comment.setReplyComments(findReply(comments, comment.getId()));
                //这就是我们最终数组中的Comment
                list.add(comment);
            }
        }
        return list;
    }

    /**
     * @param comments 我们所有的该博客下的评论
     * @param targetId 我们要查到的目标父id
     * @return 返回该评论下的所有评论
     */
    public List<Comments> findReply(List<Comments> comments, int targetId) {
        //第一级别评论的子评论集合
        List<Comments> reply = new ArrayList<>();
        for (Comments comment : comments) {
            //发现该评论的父id为targetId就将这个评论加入子评论集合
            if (find(comment.getParentId(), targetId)) {
                reply.add(comment);
            }
        }
        return reply;
    }

    public boolean find(int id, int target) {
        //不将第一节评论本身加入自身的子评论集合
        if (id == -1) {
            return false;
        }
        //如果父id等于target，那么该评论就是id为target评论的子评论
        if (id == target) {
            return true;
        } else {
            //否则就再向上找
            return find(commentsMapper.selectById(id).getParentId(), target);
        }
    }
}