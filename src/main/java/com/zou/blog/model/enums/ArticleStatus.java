package com.zou.blog.model.enums;

public enum ArticleStatus {
	/**
	 * 已发布
	 */
	PUBLISH(0, "已发布"),
	/**
	 * 草稿
	 */
	DRAFT(1, "草稿"),
	/**
	 * 回收站
	 */
	RECYCLE(2, "回收站");

	private Integer status;
	private String desc;

	private ArticleStatus(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
