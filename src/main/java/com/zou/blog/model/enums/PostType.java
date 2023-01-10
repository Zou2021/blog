package com.zou.blog.model.enums;

public enum PostType {
	/**
	 * 文章
	 */
	POST_TYPE_POST("post"),

	/**
	 * 页面
	 */
	POST_TYPE_PAGE("page");

	private String value;

	private PostType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
