package com.zou.blog.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: 邹祥发
 * @date: 2022/7/12 08:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("article_comments")
public class Comments {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String nickname;
    private String email;
    private String content;
    private Date createTime;
    private Integer blogId;
    private Integer isVisible;
    private String avatar;
    private String blogUrl;
    private String province;
    private String ip;
    private Date updateTime;
    private Integer sort;
    //评论的父节点id
    private Integer parentId;
    private String parentName;
    //回复评论
    @TableField(exist = false)
    private List<Comments> replyComments = new ArrayList<>();
    @TableField(exist = false)
    private Comments parentComment;
}