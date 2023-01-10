package com.zou.blog.service;

import java.util.List;
import java.util.Map;

import com.zou.blog.model.domain.Options;

public interface OptionsService {
	/**
	 * 保存多个
	 * 
	 * @param map
	 */
	void save(Map<String, String> map) throws Exception;

	/**
	 * 所有设置选项
	 * 
	 * @return
	 */
	List<Options> selectMap();

	/**
	 * 保存单个
	 * 
	 * @param key
	 * @param value
	 */
	void saveOption(String key, String value);

	/**
	 * 删除
	 * 
	 * @param options
	 */
	void delete(Options options) throws Exception;

}
