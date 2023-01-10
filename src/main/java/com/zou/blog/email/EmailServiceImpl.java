package com.zou.blog.email;

import com.zou.blog.email.model.EmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件服务
 *
 * @author 14470
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    JavaMailSenderImpl mailSender;

    @Value("1565453341@qq.com")
    private String emailFrom;

    /**
     * 评论发送提醒邮件
     *
     * @param email
     */
    @Override
    @Async("myThreadPool")
    public void sendSimpleEmail(EmailDto email) {
        MimeMessageHelper helper;
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessage message = mailSender.createMimeMessage();
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("召田最帅boy博客评论提醒");
            helper.setText(
                    "<html><body><p>刚刚有人回复了您</p>" +
                            "回复内容如下: "
                            + "<p style=\"white-space: pre-wrap; background:#fafafa repeating-linear-gradient(-45deg,#fff,#fff 1.125rem,transparent 1.125rem,transparent 2.25rem);box-shadow:0 2px 5px rgba(0,0,0,.15);margin:20px 0;padding:15px;border-radius:5px;font-size:14px;color:#555\">" + email.getContent() + "</p>"
                            + "评论博客地址: <a href='" + email.getUrl() + "'>查看详情</a>"
                            + "<p>若此邮件不是您请求的，请忽略并删除！</p></body></html>"
                    , true)
            ;
            helper.setTo(email.getEmailTo());
            helper.setFrom(emailFrom);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }
}