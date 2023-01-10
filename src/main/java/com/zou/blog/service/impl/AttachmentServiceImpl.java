package com.zou.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zou.blog.mapper.generator.AttachmentMapper;
import com.zou.blog.model.domain.Attachment;
import com.zou.blog.model.domain.AttachmentExample;
import com.zou.blog.service.AttachmentService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AttachmentServiceImpl implements AttachmentService {

	private static final String ATTACHMENTS_CACHE_NAME = "attachments";

	@Autowired
	private AttachmentMapper attachmentMapper;

	@Override
	@CacheEvict(value = ATTACHMENTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
	public void save(Attachment attachment) throws Exception {
		attachmentMapper.insert(attachment);
	}

	@Override
	public PageInfo<Attachment> getAttachment(int page, int limit) {
		PageHelper.startPage(page, limit);
		AttachmentExample example = new AttachmentExample();
		example.setOrderByClause("id desc");
		List<Attachment> lists = attachmentMapper.selectByExample(example);
		return new PageInfo<>(lists);
	}

	@Override
	@Cacheable(value = ATTACHMENTS_CACHE_NAME, key = "'findById'+#id")
	public Attachment findById(int id) {
		return attachmentMapper.selectByPrimaryKey(id);
	}

	@Override
	@CacheEvict(value = ATTACHMENTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
	public void deleteAttachment(int id) throws Exception {
		attachmentMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Cacheable(value = ATTACHMENTS_CACHE_NAME, key = "'countAttachment'")
	public List<Attachment> countAttachment() {
		return attachmentMapper.selectByExample(null);
	}

}
