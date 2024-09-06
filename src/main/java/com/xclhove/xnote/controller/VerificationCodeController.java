package com.xclhove.xnote.controller;


import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.VerificationCodeException;
import com.xclhove.xnote.interceptor.IpInterceptor;
import com.xclhove.xnote.service.VerificationCodeService;
import com.xclhove.xnote.tool.Result;
import com.xclhove.xnote.tool.ThreadLocalTool;
import com.xclhove.xnote.util.ValidateCodeImageUtil;
import com.xclhove.xnote.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/verification-code")
@RequiredArgsConstructor
@Validated
@Slf4j
public class VerificationCodeController {
    private final VerificationCodeService verificationCodeService;
    private final StringRedisTemplate stringRedisTemplate;
    
    public String getRedisKeyByIpAddress(String ipAddress) {
        return RedisKey.join(RedisKey.VerificationCode.IP, ipAddress);
    }
    
    public String generateImageCode() {
        String simpleCode = VerificationCodeUtil.generate(4, true);
        stringRedisTemplate.opsForValue().set(
                getRedisKeyByIpAddress(ThreadLocalTool.getClientIpAddress()),
                simpleCode,
                60,
                TimeUnit.SECONDS
        );
        return simpleCode;
    }
    
    /**
     * 发送验证码到邮箱
     */
    @GetMapping("/send/to-email")
    @IpInterceptor.PathFrequencyLimit(maxFrequencyPerMinute = 3, message = "发送验证码过于频繁")
    public Result<Integer> sendVerificationCodeToEmail(
            @NotBlank(message = "邮箱不能为空")
            @Email(message = "邮箱格式不正确")
            @RequestParam
            String email,
            @NotBlank(message = "图片验证码不能为空")
            @RequestParam
            String imageCode
    ) {
        String redisKey = getRedisKeyByIpAddress(ThreadLocalTool.getClientIpAddress());
        String codeInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isBlank(codeInRedis)) {
            throw new VerificationCodeException("验证码已过期，请重新获取");
        }
        if (!codeInRedis.equals(imageCode)) {
            throw new VerificationCodeException("图片验证码错误");
        }
        stringRedisTemplate.delete(redisKey);
        
        int expireSeconds = verificationCodeService.sendToEmail(email);
        return Result.success(expireSeconds);
    }
    
    /**
     * 获取图片验证码的base64
     */
    @GetMapping("/image/base64")
    @IpInterceptor.PathFrequencyLimit(maxFrequencyPerMinute = 10, message = "获取图片验证码过于频繁")
    public Result<String> getVerificationCodeImageBase64() {
        String simpleCode = generateImageCode();
        try {
            String imageBase64 = ValidateCodeImageUtil.generateImageBase64(simpleCode);
            return Result.success(imageBase64);
        } catch (IOException e) {
            throw new VerificationCodeException("获取图片验证码失败，请稍后再试");
        }
    }
    
    /**
     * 获取图片验证码
     */
    @GetMapping("/image")
    @IpInterceptor.PathFrequencyLimit(maxFrequencyPerMinute = 10, message = "获取图片验证码过于频繁")
    public void getVerificationCodeImage(HttpServletResponse response) {
        String simpleCode = generateImageCode();
        try {
            BufferedImage bufferedImage = ValidateCodeImageUtil.generateImage(simpleCode);
            String contentType = ValidateCodeImageUtil.CONTENT_TYPE;
            response.setContentType(contentType);
            ImageIO.write(bufferedImage, contentType.substring(contentType.indexOf("/") + 1), response.getOutputStream());
        } catch (IOException e) {
            throw new VerificationCodeException("获取图片验证码失败，请稍后再试");
        }
    }
}
