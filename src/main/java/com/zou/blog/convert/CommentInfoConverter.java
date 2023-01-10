package com.zou.blog.convert;

import com.zou.blog.model.domain.Comments;
import com.zou.blog.model.enums.CommentStatus;
import com.zou.blog.util.IpUtils;
import org.mapstruct.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: 邹祥发
 * @date: 2022/9/8 13:46
 */
@Mapper(componentModel = "spring")
public interface CommentInfoConverter {
    default Comments map(HttpServletRequest request) throws Exception {
        String ip = IpUtils.getIpAddr(request);
        String province = IpUtils.getIpPossession(ip);
        Comments comments = new Comments();
        comments.setContent(request.getParameter("content"));
        comments.setEmail(request.getParameter("email"));
        comments.setCreateTime(new Date());
        comments.setBlogId(Integer.valueOf(request.getParameter("blogid")));
        comments.setBlogUrl(request.getParameter("blogUrl"));
        comments.setProvince(province);
        comments.setIp(ip);
        comments.setUpdateTime(new Date());
        int parentId = Integer.parseInt(request.getParameter("parentId"));
        //数值越大则优先展示
        if (parentId == -1) {
            comments.setSort(1);
        } else {
            comments.setSort(Integer.parseInt(String.valueOf(System.currentTimeMillis() / 990)));
        }
        //未审核的评论默认不可见
        //暂时可见
        comments.setIsVisible(CommentStatus.VISIBLE.getStatus());
        //设置父节点id，-1为首节点
        comments.setParentId(parentId);
        comments.setParentName(request.getParameter("parentName"));
        switch (request.getParameter("email")) {
            case "1565453341@qq.com":
                comments.setNickname("召田最帅boy");
                comments.setAvatar("https://img-blog.csdnimg.cn/a4e106d8f8934ad397f852ebfeba8c55.png");
                break;
            case "1194165072@qq.com":
                comments.setNickname("琪");
                comments.setAvatar("https://img-blog.csdnimg.cn/0381d4b0e4234286a4c7d6cb45d1701f.bmp");
                break;
            default:
                comments.setNickname(request.getParameter("nickname"));
                comments.setAvatar(request.getParameter("avatar"));
                break;
        }
        return comments;
    }
}