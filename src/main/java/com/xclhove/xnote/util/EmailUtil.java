package com.xclhove.xnote.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮件工具类
 *
 * @author xclhove
 */
public class EmailUtil {
    /**
     * 发送邮件
     *
     * @param host     主机名称或IP地址，如：smtp.qq.com
     * @param port     端口号，如：465，587，25
     * @param username 邮箱账号，如：example@qq.com
     * @param password 邮箱密码
     * @param from     发送方邮箱地址，如：example@qq.com
     * @param to       接收方邮箱地址，如：example@qq.com
     * @param subject  邮件主题
     * @param text     邮件正文
     * @throws MessagingException 异常信息
     */
    public static void sendEmail(String host, int port, String username, String password, String from, String to, String subject, String text) throws MessagingException {
        // 配置JavaMail API的属性
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.trust", host);
        
        Session session = Session.getInstance(properties);
        
        // 创建邮件对象
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        String finalText = "\n------------------分割线------------------\n";
        message.setText(text + finalText);
        
        // 发送邮件
        Transport transport = session.getTransport("smtp");
        transport.connect(host, port, username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}