package com.zou.blog.email.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String emailTo;
    private String content;
    private String blogTitle;
    private String url;
}
