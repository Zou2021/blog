package com.zou.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.zou.blog.mapper.custom.ArticleMapperCustom;
import com.zou.blog.mapper.custom.CategoryMapperCustom;
import com.zou.blog.mapper.custom.CommentsMapper;
import com.zou.blog.mapper.custom.TagMapperCustom;
import com.zou.blog.mapper.generator.ArticleCategoryMapper;
import com.zou.blog.mapper.generator.ArticleMapper;
import com.zou.blog.mapper.generator.ArticleTagMapper;
import com.zou.blog.model.domain.*;
import com.zou.blog.model.dto.ArchiveBo;
import com.zou.blog.model.enums.ArticleStatus;
import com.zou.blog.model.enums.PostType;
import com.zou.blog.service.ArticleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ArticleServiceImpl implements ArticleService {

    private static final String ARTICLES_CACHE_KEY = "'article'";
    private static final String DATE_FORMAT = "yyyy年MM月";
    private static final String ARTICLES_CACHE_NAME = "articles";
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private ArticleCategoryMapper articleCategoryMapper;
    @Resource
    private ArticleMapperCustom articleMapperCustom;
    @Resource
    private TagMapperCustom tagMapperCustom;
    @Resource
    private CategoryMapperCustom categoryMapperCustom;
    @Resource
    private CommentsMapper commentsMapper;

    /**
     * 自动生成的mapper里配置了useGeneratedKeys="true" keyProperty="id" 如重新生成请复制过去
     */
    @Override
    @CacheEvict(value = ARTICLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void save(Article article, Long[] tags, Long[] categorys) throws Exception {
        articleMapper.insert(article);
        updateCategoryAndTag(article, tags, categorys);
    }

    private void updateCategoryAndTag(Article article, Long[] tags, Long[] categorys) {
        if (categorys != null) {
            Arrays.stream(categorys).forEach(cate -> {
                ArticleCategory articleCategory = new ArticleCategory();
                articleCategory.setArticleId(article.getId());
                articleCategory.setCategoryId(cate);
                articleCategoryMapper.insert(articleCategory);
            });
        }
        if (tags != null) {
            Arrays.stream(tags).forEach(tag -> {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag);
                articleTagMapper.insert(articleTag);
            });
        }
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'findAllArticle'+#status+#post")
    public List<ArticleCustom> findAllArticle(int status, String post) {
        return articleMapperCustom.findAllArticle(status, post);
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'findPageArticle'+#page+#limit+#articleCustom.articleStatus+#articleCustom.articlePost")
    public PageInfo<ArticleCustom> findPageArticle(int page, int limit, ArticleCustom articleCustom) {
        PageMethod.startPage(page, limit);
        List<ArticleCustom> lists = articleMapperCustom.findPageArticle(articleCustom);
        return new PageInfo<>(lists);
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'Post_status'+#status+#post")
    public Integer countByStatus(Integer status, String post) {
        return articleMapperCustom.countByStatus(status, post);
    }

    @Override
    @CacheEvict(value = ARTICLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void recycle(int id, Integer integer) throws Exception {
        articleMapperCustom.updateStatus(id, integer);
    }

    @Override
    @CacheEvict(value = ARTICLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void remove(int id) throws Exception {
        // 删除文章表
        articleMapper.deleteByPrimaryKey(id);
        ArticleTagExample articleTagexample = new ArticleTagExample();
        ArticleTagExample.Criteria tagCriteria = articleTagexample.createCriteria();
        tagCriteria.andArticleIdEqualTo(id);
        ArticleCategoryExample articleCategoryExample = new ArticleCategoryExample();
        ArticleCategoryExample.Criteria categoryCriteria = articleCategoryExample.createCriteria();
        categoryCriteria.andArticleIdEqualTo(id);
        // 删除分类表和标签表
        articleTagMapper.deleteByExample(articleTagexample);
        articleCategoryMapper.deleteByExample(articleCategoryExample);
        //删除相关评论
        commentsMapper.delete(new QueryWrapper<Comments>().eq("blog_id", id));
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'findByArticleId'+#article_id")
    public ArticleCustom findByArticleId(Integer article_id) {
        return articleMapperCustom.findByArticleId(article_id);
    }

    @Override
    @CacheEvict(value = ARTICLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void update(Article article, Long[] tags, Long[] categorys) throws Exception {
        // 修改文章
        articleMapper.updateByPrimaryKeySelective(article);
        // 先查出关联的分类与标签
        List<Integer> tagList = tagMapperCustom.selectByarticleId(article.getId());
        List<Integer> cateList = categoryMapperCustom.selectByarticleId(article.getId());
        if (tagList != null && !tagList.isEmpty()) {
            // 然后删除
            tagMapperCustom.delete(tagList, article.getId());
        }
        if (cateList != null && !cateList.isEmpty()) {
            categoryMapperCustom.delete(cateList, article.getId());
        }
        // 再添加
        updateCategoryAndTag(article, tags, categorys);
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = ARTICLES_CACHE_KEY)
    public List<ArchiveBo> archives() {
        // 查询文章表各个时间段的文章数量 分别为DATE->时间段 count->文章数量
        List<ArchiveBo> listforArchiveBo = articleMapperCustom.findDateAndCount();
        if (listforArchiveBo != null) {
            for (ArchiveBo archiveBo : listforArchiveBo) {
                QueryWrapper<Article> wrapper = new QueryWrapper<>();
                String date = archiveBo.getDate();
                Date sd = DateUtil.parse(date, DATE_FORMAT);
                // 在wrapper对象中放入article_newstime大于或者等于的值
                wrapper.ge("article_newstime", sd);
                Calendar cal = Calendar.getInstance();
                // 判断获取的时间的月份是否小于12
                if (cal.get(Calendar.MONTH) < 12) {
                    cal.setTime(sd);
                    // 月份 +1
                    cal.add(Calendar.MONTH, +1);
                } else {
                    cal.setTime(sd);
                    // 年 +1
                    cal.add(Calendar.YEAR, +1);
                }
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                // 在wrapper对象中放入article_newstime小于的值
                wrapper.eq("article_status", 0);
                wrapper.orderByDesc("article_newstime");
                wrapper.lt("article_newstime", DateUtil.parse(sdf.format(cal.getTime()), DATE_FORMAT));
                //只查询所需字段，加快检索速度
                List<Article> articles = articleMapper.selectList(wrapper.select("article_url", "article_title", "article_newstime"));
                archiveBo.setArticles(articles);
            }
        }
        return listforArchiveBo;
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'articleUrlInt'+#articleUrl")
    public int findRepeatByUrl(String articleUrl) {
        return articleMapperCustom.findRepeatByUrl(articleUrl);
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'articleUrl'+#articleUrl")
    public ArticleCustom findByArticleUrl(String articleUrl, Integer status) {
        return articleMapperCustom.findByArticleUrl(articleUrl, status);
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'findArtileByCategory'+#page+#limit+#category.categoryUrl")
    public PageInfo<ArticleCustom> findArtileByCategory(int page, int limit, Category category, int status) {
        PageMethod.startPage(page, limit);
        List<ArticleCustom> list = articleMapperCustom.findArtileByCategory(category, status);
        return new PageInfo<>(list);
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'findArtileByTag'+#page+#limit+#status+#tag.tagUrl")
    public PageInfo<ArticleCustom> findArtileByTag(Integer page, Integer limit, Tag tag, int status) {
        PageMethod.startPage(page, limit);
        List<ArticleCustom> list = articleMapperCustom.findArtileByTag(tag, status);
        return new PageInfo<>(list);
    }

    @Override
    @Cacheable(value = ARTICLES_CACHE_NAME, key = "'findArticleByKeywords'+#keywords+#page+#limit")
    public PageInfo<Article> findArticleByKeywords(String keywords, Integer page, Integer limit) {
        PageMethod.startPage(page, limit);
        ArticleExample articleExample = new ArticleExample();
        ArticleExample.Criteria criteria = articleExample.createCriteria();
        criteria.andArticlePostEqualTo(PostType.POST_TYPE_POST.getValue());
        criteria.andArticleStatusEqualTo(ArticleStatus.PUBLISH.getStatus());
        criteria.andArticleTitleLike("%" + keywords + "%");
        //全文检索
        articleExample.or().andArticleContentLike("%" + keywords + "%")
                .andArticleStatusEqualTo(ArticleStatus.PUBLISH.getStatus());
        List<Article> list = articleMapper.selectByExample(articleExample);
        return new PageInfo<>(list);
    }

    @Override
    @CacheEvict(value = ARTICLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void updateArticleViews(Article article) {
        articleMapper.updateByPrimaryKeySelective(article);
    }

    /**
     * 统计文章字数
     *
     * @param id
     * @return
     */
    @Override
    public int countArticleNum(Integer id) {
        return articleMapper.countArticleNum(id);
    }
}
