package com.xclhove.xnote.runner;

import com.xclhove.xnote.config.JavaMailConfig;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.tool.EmailTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * 启动时检查能否发送邮件
 *
 * @author xclhove
 */
@Component
@Slf4j
@RequiredArgsConstructor
public final class EmailRunner implements ApplicationRunner {
    private final EmailTool emailTool;
    private final JavaMailConfig javaMailConfig;
    private final XnoteConfig xnoteConfig;
    
    @Override
    public void run(ApplicationArguments args) {
        if (!xnoteConfig.runner.getSendStartupEmail()) {
            log.info("不发送启动通知邮件！");
            return;
        }
        sendStartupEmail();
    }
    
    private void sendStartupEmail() {
        try {
            emailTool.sendMail(javaMailConfig.getUsername(), "XNote启动成功！", "XNote启动成功！");
            log.info("启动通知邮件发送成功！");
        } catch (MessagingException e) {
            log.error("启动通知邮件发送失败", e);
            System.exit(1);
        }
    }
}
