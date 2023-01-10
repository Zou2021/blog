package com.zou.blog.email;

import com.zou.blog.email.model.EmailDto;

public interface EmailService {
    void sendSimpleEmail(EmailDto email);
}
