package com.xclhove.xnote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author xclhove
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xnote")
@EnableConfigurationProperties(XnoteConfig.class)
public class XnoteConfig {
    @NestedConfigurationProperty
    public Debug debug = new Debug();
    @NestedConfigurationProperty
    public Image image = new Image();
    @NestedConfigurationProperty
    public Runner runner = new Runner();
    @NestedConfigurationProperty
    public Search search = new Search();
    @NestedConfigurationProperty
    public Interceptor interceptor = new Interceptor();
    
    @Data
    public static class Debug {
        /**
         * 是否开启验证码验证
         */
        private Boolean enableVerifyVerificationCode = true;
        /**
         * 是否开启模拟发送验证码
         */
        private Boolean enableMockSendVerificationCode = false;
    }
    
    @Data
    public static class Image {
        /**
         * 允许上传的图片大小(B)，默认10M
         */
        private Long allowSizeOfByte = 10485760L;
    }
    
    @Data
    public static class Runner {
        /**
         * 是否需要发送启动邮件（检查发送邮件功能是否正常）
         */
        private Boolean sendStartupEmail = false;
        /**
         * 是否需要检查Minio服务状态
         */
        private Boolean enableCheckMinioStatus = true;
        /**
         * 是否需要检查Redis服务状态
         */
        private Boolean enableCheckRedisStatus = true;
        /**
         * 是否需要在启动时导入数据到ElasticSearch
         */
        private Boolean importDataFromDatabaseToElasticSearch = false;
        /**
         * 启动时更新图片大小
         */
        private Boolean updateImageSize = false;
        /**
         * 启动时为用户创建默认笔记类型
         */
        private Boolean createDefaultNoteTypeForUserOfNoneType = false;
        /**
         * 启动时替换笔记中的图片 url
         */
        private Boolean replaceImageUrlInNote = false;
    }
    
    @Data
    public static class Search {
        /**
         * 搜索结果最大条数
         */
        private int heightLightContentMaxLength = 200;
    }
    
    @Data
    public static class Interceptor {
        public Ip ip = new Ip();
        public Device device = new Device();
        
        @Data
        public static class Ip {
            /**
             * 是否禁用Ip拦截器
             */
            private Boolean disable = false;
            /**
             * 单个ip每分钟最大请求次数
             */
            private int maxFrequencyPerMinute = 120;
        }
        
        @Data
        public static class Device {
            /**
             * 是否禁用Device拦截器
             */
            private Boolean disable = false;
            /**
             * 单个设备每分钟最大请求次数
             */
            private int maxFrequencyPerMinute = 120;
        }
    }
}
