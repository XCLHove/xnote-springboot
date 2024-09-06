package com.xclhove.xnote.tool;

import com.xclhove.xnote.config.JavaMailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送工具类
 *
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
public final class EmailTool {
    private final JavaMailConfig javaMailConfig;
    private final JavaMailSender javaMailSender;
    
    /**
     * 发送邮件
     * @param to 接收者
     * @param subject 主题
     * @param content 内容
     * @throws MessagingException 邮件发送异常
     */
    public void sendMail(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(new InternetAddress(javaMailConfig.getUsername()));
        mimeMessage.setRecipients(Message.RecipientType.TO, to);
        mimeMessage.setSubject(subject);
        mimeMessage.setText(content);
        javaMailSender.send(mimeMessage);
    }
}