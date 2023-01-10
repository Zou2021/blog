package com.zou.blog.service;

import java.util.List;

import com.zou.blog.model.domain.Link;

public interface LinksService {
	/**
	 * 查询所有友情链接
	 * 
	 * @return
	 */
	List<Link> findLinks();

	/**
	 * 根据linkId查询
	 * 
	 * @param linkId
	 *            友链id
	 * @return
	 */
	Link findByLindId(int linkId);

	/**
	 * 保存
	 * 
	 * @param link
	 * @throws Exception
	 */
	public void save(Link link) throws Exception;

	/**
	 * 修改
	 * 
	 * @param link
	 */
	void update(Link link);

	/**
	 * 删除
	 * 
	 * @param linkId
	 */
	void remove(int linkId);

}
