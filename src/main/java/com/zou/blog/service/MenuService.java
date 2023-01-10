package com.zou.blog.service;

import java.util.List;

import com.zou.blog.model.domain.Menu;

public interface MenuService {
	/**
	 * @return 所有菜单
	 */
	List<Menu> findMenus();

	/**
	 * 根据id查询
	 * 
	 * @param menuId
	 * @return
	 */
	Menu findByMenuId(Integer menuId);

	/**
	 * 添加
	 * 
	 * @param menu
	 */
	void save(Menu menu);

	/**
	 * 修改
	 * 
	 * @param menu
	 */
	void edit(Menu menu);

	/**
	 * 移除
	 * 
	 * @param menuId
	 */
	void remove(Integer menuId);

}
