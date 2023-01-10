package com.zou.blog.mapper.generator;

import com.zou.blog.model.domain.Options;
import com.zou.blog.model.domain.OptionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OptionsMapper {
	long countByExample(OptionsExample example);

	int deleteByExample(OptionsExample example);

	int deleteByPrimaryKey(String optionName);

	int insert(Options record);

	int insertSelective(Options record);

	List<Options> selectByExample(OptionsExample example);

	Options selectByPrimaryKey(String optionName);

	int updateByExampleSelective(@Param("record") Options record, @Param("example") OptionsExample example);

	int updateByExample(@Param("record") Options record, @Param("example") OptionsExample example);

	int updateByPrimaryKeySelective(Options record);

	int updateByPrimaryKey(Options record);
}