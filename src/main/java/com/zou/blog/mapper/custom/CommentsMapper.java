package com.zou.blog.mapper.custom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zou.blog.model.domain.Comments;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 邹祥发
 * @date: 2022/7/12 08:11
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
}
