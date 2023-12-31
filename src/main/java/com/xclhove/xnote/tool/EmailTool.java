package com.xclhove.xnote.tool;

import com.xclhove.xnote.config.MailConfig;
import com.xclhove.xnote.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * 邮件发送工具类
 *
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
public class EmailTool {
    private final MailConfig mailConfig;
    
    /**
     * 发送邮件
     * @param to 接收者
     * @param subject 主题
     * @param content 内容
     * @throws MessagingException 邮件发送异常
     */
    public void sendMail(String to, String subject, String content) throws MessagingException {
        EmailUtil.sendEmail(mailConfig.getHost(),
                mailConfig.getPort(),
                mailConfig.getUsername(),
                mailConfig.getPassword(),
                mailConfig.getUsername(),
                to,
                subject,
                content);
    }
}