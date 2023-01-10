package com.zou.blog.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TagMapperCustom {

	List<Integer> selectByarticleId(Integer id);

	void delete(@Param(value = "list") List<Integer> tagList, @Param(value = "articleId") Integer articleId);

}
