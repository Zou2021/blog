package com.zou.blog.mapper.custom;

import org.apache.ibatis.annotations.Param;

public interface ThemeMapperCustom {

	void updateStatus(@Param(value="status")int status,@Param(value="id") int id);

}
