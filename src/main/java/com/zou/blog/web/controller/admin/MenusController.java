package com.zou.blog.web.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zou.blog.model.domain.Menu;
import com.zou.blog.model.dto.MaydayConst;
import com.zou.blog.service.MenuService;

/**
 * 菜单
 */
@RequestMapping("/admin/menus")
@Controller
public class MenusController extends BaseController {
	@Autowired
	private MenuService menuService;

	/**
	 * @param model
	 * @return 进入菜单
	 */
	@GetMapping
	public String menus(Model model) {
		model.addAttribute("menus", MaydayConst.MENUS);
		return "admin/admin_menus";
	}

	/**
	 * @param model
	 * @param menuId
	 * @return 编辑页面
	 */
	@GetMapping(value = "/edit")
	public String edit(Model model, @RequestParam(value = "menuId") Integer menuId) {
		Menu menu = menuService.findByMenuId(menuId);
		List<Menu> menus = menuService.findMenus();
		model.addAttribute("menus", menus);
		model.addAttribute("menu", menu);
		return "admin/admin_menus";
	}

	/**
	 * 保存或修改
	 * 
	 * @param menu
	 * @return
	 */
	@PostMapping(value = "/save")
	public String save(Menu menu) {
		try {
			if (menu.getMenuId() == null) {
				menuService.save(menu);
			} else {
				menuService.edit(menu);
			}
			MaydayConst.MENUS.clear();
			MaydayConst.MENUS = menuService.findMenus();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "redirect:/admin/menus";
	}

	@GetMapping("/remove")
	public String remove(@RequestParam(value = "menuId") Integer menuId) {
		try {
			menuService.remove(menuId);
			MaydayConst.MENUS.clear();
			MaydayConst.MENUS = menuService.findMenus();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "redirect:/admin/menus";
	}
}
