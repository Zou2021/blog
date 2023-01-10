package com.zou.blog.service.impl;

import com.zou.blog.mapper.generator.TagMapper;
import com.zou.blog.model.domain.Tag;
import com.zou.blog.model.domain.TagExample;
import com.zou.blog.service.TagService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class TagServiceImpl implements TagService {
    private static final String TAGS_CACHE_KEY = "'tag'";
    private static final String TAGS_CACHE_NAME = "tags";
    @Resource
    private TagMapper tagMapper;

    @Override
    @Cacheable(value = TAGS_CACHE_NAME, key = TAGS_CACHE_KEY)
    public List<Tag> findTags() {
        return tagMapper.selectByExample(null);
    }

    @Override
    public Tag findByTagId(int linkId) {
        return tagMapper.selectByPrimaryKey(linkId);
    }

    @Override
    @CacheEvict(value = TAGS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void save(Tag tag) throws Exception {
        tagMapper.insert(tag);
    }

    @Override
    @CacheEvict(value = TAGS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void update(Tag tag) throws Exception {
        tagMapper.updateByPrimaryKeySelective(tag);
    }

    @Override
    @CacheEvict(value = TAGS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void remove(int tagId) throws Exception {
        tagMapper.deleteByPrimaryKey(tagId);
    }

    @Override
    public Tag findByTagUrl(String tagUrl) {
        TagExample tagExample = new TagExample();
        TagExample.Criteria criteria = tagExample.createCriteria();
        criteria.andTagUrlEqualTo(tagUrl);
        return tagMapper.selectByExample(tagExample).get(0);
    }
}