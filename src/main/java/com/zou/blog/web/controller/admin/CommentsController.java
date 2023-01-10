package com.zou.blog.web.controller.admin;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zou.blog.model.domain.Comments;
import com.zou.blog.model.domain.Log;
import com.zou.blog.model.dto.LogConstant;
import com.zou.blog.model.enums.CommentStatus;
import com.zou.blog.service.CommentService;
import com.zou.blog.util.IpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author: 邹祥发
 * @date: 2022/7/14 09:01
 */

@Controller
@RequestMapping("/admin/comments")
public class CommentsController extends BaseController {

    @Resource
    private BaseMapper<Comments> baseMapper;

    @Resource
    private CommentService commentService;

    private static final String URL = "redirect:/admin/comments";

    /**
     * 评论列表，未审核优先展示
     */
    @GetMapping
    public String viewComments(Model model) {
        List<Comments> isNotVisibleComments = commentService.viewIsNotVisibleComments();
        model.addAttribute("isNotVisibleComments", isNotVisibleComments);
        return "admin/admin_comments";
    }

    /**
     * 通过审核
     */
    @GetMapping(value = "/visible/{id}")
    public String visible(@PathVariable("id") int id, HttpServletRequest request) {
        try {
            UpdateWrapper<Comments> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("is_visible", CommentStatus.VISIBLE.getStatus());
            wrapper.set("update_time", new Date());
            baseMapper.update(null, wrapper);
            // 添加日志
            logService.save(new Log(LogConstant.REVIEW_COMMENT, LogConstant.SUCCESS,
                    IpUtils.getIpAddr(request), DateUtil.date()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return URL;
    }

    /**
     * 删除评论
     */
    @GetMapping(value = "/remove/{id}")
    public String remove(@PathVariable("id") int id, HttpServletRequest request) {
        try {
            commentService.remove(id);
            // 添加日志
            logService.save(new Log(LogConstant.DELETE_COMMENT, LogConstant.SUCCESS,
                    IpUtils.getIpAddr(request), DateUtil.date()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return URL;
    }

    /**
     * 屏蔽评论
     */
    @GetMapping(value = "/invisible/{id}")
    public String invisible(@PathVariable("id") int id, HttpServletRequest request) {
        try {
            UpdateWrapper<Comments> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("is_visible", CommentStatus.INVISIBLE.getStatus());
            wrapper.set("update_time", new Date());
            baseMapper.update(null, wrapper);
            logService.save(new Log(LogConstant.DISABLED_COMMENT, LogConstant.SUCCESS,
                    IpUtils.getIpAddr(request), DateUtil.date()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return URL;
    }

}
