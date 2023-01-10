package com.zou.blog.service;

import com.zou.blog.model.domain.Comments;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: 邹祥发
 * @date: 2022/7/12 08:08
 */
public interface CommentService {
    /**
     * 添加评论
     *
     * @param comments
     */
    @Transactional(rollbackFor = RuntimeException.class)
    void save(Comments comments, int parentCommentId);

    /**
     * 评论列表
     *
     * @param blogId
     * @return
     */
    List<Comments> listCommentByBlogId(int blogId);

    /**
     * 评论列表，未审核优先展示
     *
     * @return
     */
    List<Comments> viewIsNotVisibleComments();

    /**
     * 删除评论
     *
     * @param id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    void remove(int id);

    long commentCount(int id);
}
