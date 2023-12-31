package com.xclhove.xnote.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 邮件配置
 *
 * @author xclhove
 */
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class MailConfig {
    private String host;
    
    private String username;
    
    private String password;
    
    private int port;
}