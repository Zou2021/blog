package com.zou.blog.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zou.blog.model.domain.ArticleCustom;
import com.zou.blog.model.domain.Category;
import com.zou.blog.model.domain.Tag;
import com.zou.blog.model.dto.ArchiveBo;

public interface ArticleMapperCustom {

    List<ArticleCustom> articleMapperCustom(@Param(value = "status") int status);

    List<ArticleCustom> findAllArticle(@Param(value = "status") int status, @Param(value = "post") String post);

    Integer countByStatus(@Param(value = "status") Integer status, @Param(value = "post") String post);

    void updateStatus(@Param(value = "id") int id, @Param(value = "status") int status);

    ArticleCustom findByArticleId(@Param(value = "id") Integer article_id);

    List<ArchiveBo> findDateAndCount();

    List<ArticleCustom> findPageArticle(ArticleCustom articleCustom);

    int findRepeatByUrl(@Param(value = "articleUrl") String articleUrl);

    ArticleCustom findByArticleUrl(@Param(value = "articleUrl") String articleUrl, @Param(value = "status") int status);

    List<ArticleCustom> findArtileByCategory(@Param("category") Category category, @Param(value = "status") int status);

    List<ArticleCustom> findArtileByTag(@Param("tag") Tag tag, @Param(value = "status") int status);
}
