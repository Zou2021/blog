package com.zou.blog.service;

import com.github.pagehelper.PageInfo;
import com.zou.blog.model.domain.Theme;

public interface ThemeService {
	/**
	 * 分页
	 * @param page
	 * @param limit
	 * @return
	 */
	PageInfo<Theme> findPageTheme(int page, int limit);
	/**
	 * 保存主题
	 * @param theme
	 */
	void saveTheme(Theme theme);

	/**
	 * 删除主题
	 * @param id
	 */
	void remove(int id);
	/**
	 * 根据主题名查询主题
	 * @param themeName 主题名
	 * @return
	 */
	Theme findByThemeName(String themeName);
	/**
	 * 启用主题
	 * @param id
	 */
	void themeEnabled(int id);
	/**
	 * 查询已启用的主题
	 * @return 已启用的主题名
	 */
	String getEnabledTheme();

}
