package com.zou.blog.model.enums;

/**
 * @author: 邹祥发
 * @date: 2022/7/14 09:53
 */
public enum CommentStatus {
    /**
     * 未审核
     */
    INVISIBLE(0, "未审核"),
    /**
     * 已审核
     */
    VISIBLE(1, "已审核");

    private Integer status;
    private String desc;

    private CommentStatus(Integer status, String desc) {
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
