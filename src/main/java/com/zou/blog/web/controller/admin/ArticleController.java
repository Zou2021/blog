package com.zou.blog.web.controller.admin;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.zou.blog.model.domain.Article;
import com.zou.blog.model.domain.ArticleCustom;
import com.zou.blog.model.domain.Category;
import com.zou.blog.model.domain.Log;
import com.zou.blog.model.domain.Tag;
import com.zou.blog.model.domain.User;
import com.zou.blog.model.dto.JsonResult;
import com.zou.blog.model.dto.LogConstant;
import com.zou.blog.model.dto.MaydayConst;
import com.zou.blog.model.enums.ArticleStatus;
import com.zou.blog.model.enums.MaydayEnums;
import com.zou.blog.model.enums.PostType;
import com.zou.blog.service.ArticleService;
import com.zou.blog.service.CategoryService;
import com.zou.blog.service.TagService;
import com.zou.blog.util.MaydayUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HtmlUtil;

@Controller
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleService articleService;

    /**
     * 显示所有文章
     *
     * @param model
     * @param page
     * @param limit
     * @return
     */
    @GetMapping
    public String article(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "limit", defaultValue = "10") int limit,
                          @RequestParam(value = "status", defaultValue = "0") int status) {
        ArticleCustom articleCustom = new ArticleCustom();
        articleCustom.setArticleStatus(status);
        articleCustom.setArticlePost(PostType.POST_TYPE_POST.getValue());
        PageInfo<ArticleCustom> pageInfo = articleService.findPageArticle(page, limit, articleCustom);
        model.addAttribute("info", pageInfo);
        // 已发布条数
        model.addAttribute("published",
                articleService.countByStatus(ArticleStatus.PUBLISH.getStatus(), PostType.POST_TYPE_POST.getValue()));
        // 草稿条数
        model.addAttribute("draft",
                articleService.countByStatus(ArticleStatus.DRAFT.getStatus(), PostType.POST_TYPE_POST.getValue()));
        // 回收站条数
        model.addAttribute("recycle",
                articleService.countByStatus(ArticleStatus.RECYCLE.getStatus(), PostType.POST_TYPE_POST.getValue()));
        model.addAttribute("status", status);
        return "admin/admin_article";
    }

    /**
     * 过滤空格
     *
     * @param articleTitle
     * @return
     */
    @PostMapping(value = "/filter")
    @ResponseBody
    public JsonResult filter(String articleTitle) {
        articleTitle = articleTitle.replaceAll(" ", "-");
        return new JsonResult(true, articleTitle);
    }

    /**
     * 保存文章
     *
     * @param article   文章
     * @param tags      标签
     * @param categorys 分类
     * @return
     */
    @PostMapping(value = "/new/save")
    @ResponseBody
    public JsonResult save(Article article, Long[] tags, Long[] categorys, HttpServletRequest request) {
        try {
            if (CharSequenceUtil.isEmpty(article.getArticleTitle())) {
                return new JsonResult(false, "标题不能为空");
            }
            if (article.getId() == null) {
                // 判断文章链接是否重复
                if (!CharSequenceUtil.isEmpty(article.getArticleUrl())) {
                    if (article.getArticleUrl().length() > 50) {
                        return new JsonResult(false, "路径不能大于50");
                    }
                    // 查询url是否重复
                    int repeat = articleService.findRepeatByUrl(article.getArticleUrl());
                    if (repeat != 0) {
                        return new JsonResult(false, "路径已存在");
                    }
                }
                User user = (User) request.getSession().getAttribute(MaydayConst.USER_SESSION_KEY);
                int id = Integer.parseInt(String.valueOf(System.currentTimeMillis() / 100000));
                article.setId(id);
                article.setUserId(user.getUserId());
                article.setArticleNewstime(DateUtil.date());
                article.setArticleUpdatetime(DateUtil.date());
                article.setArticleViews(101L);
                if (CharSequenceUtil.isEmpty(article.getArticleUrl())) {
                    //随机生成11位字符
                    String url = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", 11);
                    article.setArticleUrl(url);
                }
                // 如果没有选择略缩图则随机一张图
                if (CharSequenceUtil.isEmpty(article.getArticleThumbnail())) {
                    article.setArticleThumbnail("/static/img/rand/" + RandomUtil.randomInt(1, 26) + ".jpg");
                }
                // 判断摘要是否为空
                if (CharSequenceUtil.isEmpty(article.getArticleSummary())) {
                    // 如果摘要为空则取前五十字为摘要
                    int postSummary = 71;
                    if (CharSequenceUtil.isNotEmpty(MaydayConst.OPTIONS.get("post_summary"))) {
                        postSummary = Integer.parseInt(MaydayConst.OPTIONS.get("post_summary"));
                    }
                    // 清理html标签和空白字符
                    String summaryText = CharSequenceUtil.cleanBlank(HtmlUtil.cleanHtmlTag(article.getArticleContent()));
                    // 设置文章摘要
                    if (summaryText.length() > postSummary) {
                        article.setArticleSummary(summaryText.substring(0, postSummary));
                    } else {
                        article.setArticleSummary(summaryText);
                    }
                }
                articleService.save(article, tags, categorys);
                // 添加日志
                logService.save(new Log(LogConstant.PUBLISH_AN_ARTICLE, LogConstant.SUCCESS,
                        ServletUtil.getClientIP(request), DateUtil.date()));
            } else {
                // 如果没有选择略缩图则随机一张图
                if (CharSequenceUtil.isEmpty(article.getArticleThumbnail())) {
                    article.setArticleThumbnail("/static/img/rand/" + RandomUtil.randomInt(1, 26) + ".jpg");
                }
                // 判断摘要是否为空
                if (CharSequenceUtil.isEmpty(article.getArticleSummary())) {
                    // 如果摘要为空则取前五十字为摘要
                    int postSummary = 71;
                    if (CharSequenceUtil.isNotEmpty(MaydayConst.OPTIONS.get("post_summary"))) {
                        postSummary = Integer.parseInt(MaydayConst.OPTIONS.get("post_summary"));
                    }
                    // 清理html标签和空白字符
                    String summaryText = CharSequenceUtil.cleanBlank(HtmlUtil.cleanHtmlTag(article.getArticleContent()));
                    // 设置文章摘要
                    if (summaryText.length() > postSummary) {
                        article.setArticleSummary(summaryText.substring(0, postSummary));
                    } else {
                        article.setArticleSummary(summaryText);
                    }
                }
                // 文章最后修改时间
                article.setArticleUpdatetime(DateUtil.date());
                articleService.update(article, tags, categorys);
                // 添加日志
                logService.save(new Log(LogConstant.UPDATE_AN_ARTICLE, LogConstant.SUCCESS,
                        ServletUtil.getClientIP(request), DateUtil.date()));
            }
        } catch (Exception e) {
            log.error("添加或更新失败" + e.getMessage());
            return new JsonResult(MaydayEnums.ERROR.isFlag(), MaydayEnums.ERROR.getMessage());
        }
        return new JsonResult(MaydayEnums.PRESERVE_SUCCESS.isFlag(), MaydayEnums.PRESERVE_SUCCESS.getMessage());
    }

    /**
     * 推送百度
     *
     * @param token 在搜索资源平台申请的推送用的准入密钥
     * @return
     */
    @PostMapping(value = "/pushBaidu")
    @ResponseBody
    public JsonResult pushBaidu(@RequestParam(value = "token") String token) {
        try {
            if (StrUtil.isEmpty(token)) {
                return new JsonResult(false, "请先填写token");
            }
            String blogUrl = MaydayConst.OPTIONS.get("blog_url");
            List<ArticleCustom> articles = articleService.findAllArticle(ArticleStatus.PUBLISH.getStatus(),
                    PostType.POST_TYPE_POST.getValue());
            StringBuffer urls = new StringBuffer();
            for (ArticleCustom article : articles) {
                urls.append(blogUrl).append("/archives/").append(article.getArticleUrl()).append("\n");
            }
            String result = MaydayUtil.baiduPost(blogUrl, token, urls.toString());
            if (StrUtil.isEmpty(result)) {
                return new JsonResult(false, "推送失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new JsonResult(true, "推送成功");
    }

    /**
     * 还原文章为发布状态
     *
     * @param id 文章id
     * @return
     */
    @PostMapping(value = "/restore")
    @ResponseBody
    public JsonResult restore(@RequestParam(value = "id") int id) {
        try {
            articleService.recycle(id, ArticleStatus.PUBLISH.getStatus());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new JsonResult(MaydayEnums.OPERATION_SUCCESS.isFlag(), MaydayEnums.OPERATION_SUCCESS.getMessage());
    }

    /**
     * 修改文章状态为回收站
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/recycle")
    public String updateStatus(@RequestParam(value = "id") int id) {
        try {
            articleService.recycle(id, ArticleStatus.RECYCLE.getStatus());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/admin/article?status=0";
    }

    /**
     * 彻底删除文章
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/remove")
    public String remove(@RequestParam(value = "id") int id, HttpServletRequest request) {
        try {
            articleService.remove(id);
            // 添加日志
            logService.save(new Log(LogConstant.REMOVE_AN_ARTICLE, LogConstant.SUCCESS,
                    ServletUtil.getClientIP(request), DateUtil.date()));
        } catch (Exception e) {
            log.error("删除文章失败" + e.getMessage());
        }
        return "redirect:/admin/article?status=2";
    }

    /**
     * 新建文章页面
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/new")
    public String newArticle(Model model) {
        try {
            List<Category> categorys = categoryService.findCategory();
            List<Tag> tags = tagService.findTags();
            model.addAttribute("categorys", categorys);
            model.addAttribute("tags", tags);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "admin/admin_new_article";
    }

    /**
     * 修改文章页面
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/edit")
    public String editArticle(Model model, @RequestParam(value = "article_id") Integer articleId) {
        try {
            // 获取所有分类
            List<Category> categorys = categoryService.findCategory();
            // 获取所有标签
            List<Tag> tags = tagService.findTags();
            // 获取文章信息
            ArticleCustom articleCustom = articleService.findByArticleId(articleId);
            model.addAttribute("categorys", categorys);
            model.addAttribute("tags", tags);
            model.addAttribute("articleCustom", articleCustom);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "admin/admin_edit_article";
    }

    /**
     * @param articleId 文章id
     * @return 该篇文章关联的分类和标签
     */
    @PostMapping(value = "/ids")
    @ResponseBody
    public Map<String, Object> ids(Integer articleId) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 获取文章信息
            ArticleCustom articleCustom = articleService.findByArticleId(articleId);
            if (articleCustom.getTags() != null) {
                map.put("tagsIds", articleCustom.getTags().split(","));
            }
            if (articleCustom.getCategorys() != null) {
                map.put("categorysIds", articleCustom.getCategorys().split(","));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return map;
    }
}