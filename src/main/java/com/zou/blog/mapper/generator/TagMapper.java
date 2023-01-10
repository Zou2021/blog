package com.zou.blog.mapper.generator;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zou.blog.model.domain.Tag;
import com.zou.blog.model.domain.TagExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TagMapper extends BaseMapper<Tag> {
    long countByExample(TagExample example);

    int deleteByExample(TagExample example);

    int deleteByPrimaryKey(Integer tagId);

    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(Integer tagId);

    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);
}