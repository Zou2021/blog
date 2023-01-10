package com.zou.blog.model.enums;

public enum PageNumber {
	POST_INDEX_lIMIT(12);

	// 分页条数
	int limit;

	private PageNumber(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
