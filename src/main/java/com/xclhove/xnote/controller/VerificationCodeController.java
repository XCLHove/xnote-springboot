package com.xclhove.xnote.controller;


import com.xclhove.xnote.exception.VerificationCodeException;
import com.xclhove.xnote.interceptor.ImageVerificationCodeInterceptor;
import com.xclhove.xnote.interceptor.IpInterceptor;
import com.xclhove.xnote.interceptor.annotations.NeedImageVerificationCode;
import com.xclhove.xnote.interceptor.annotations.PathFrequencyLimit;
import com.xclhove.xnote.service.VerificationCodeService;
import com.xclhove.xnote.tool.Result;
import com.xclhove.xnote.tool.ThreadLocalTool;
import com.xclhove.xnote.util.ValidateCodeImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    
    
    /**
     * 发送验证码到邮箱
     */
    @GetMapping("/send/to-email")
    @PathFrequencyLimit(maxFrequencyPerMinute = 3, message = "发送验证码过于频繁", needRequestSuccess = true)
    @NeedImageVerificationCode
    public Result<Integer> sendVerificationCodeToEmail(
            @NotBlank(message = "邮箱不能为空")
            @Email(message = "邮箱格式不正确")
            @RequestParam
            String email
    ) {
        int expireSeconds = verificationCodeService.sendToEmail(email);
        return Result.success(expireSeconds);
    }
    
    /**
     * 获取图片验证码的base64
     */
    @GetMapping("/image/base64")
    @PathFrequencyLimit(maxFrequencyPerMinute = 10, message = "获取图片验证码过于频繁")
    public Result<String> getVerificationCodeImageBase64() {
        String simpleCode = verificationCodeService.generateImageVerificationCode(ThreadLocalTool.getClientIpAddress());
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
    @PathFrequencyLimit(maxFrequencyPerMinute = 10, message = "获取图片验证码过于频繁")
    public void getVerificationCodeImage(HttpServletResponse response) {
        String simpleCode = verificationCodeService.generateImageVerificationCode(ThreadLocalTool.getClientIpAddress());
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
