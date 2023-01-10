package com.zou.blog.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CategoryMapperCustom {

	List<Integer> selectByarticleId(Integer id);

	void delete(@Param(value = "list") List<Integer> cateList, @Param(value = "articleId") Integer articleId);

}