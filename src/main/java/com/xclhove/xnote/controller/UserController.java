package com.xclhove.xnote.controller;


import cn.hutool.core.bean.BeanUtil;
import com.xclhove.xnote.exception.UserServiceException;
import com.xclhove.xnote.interceptor.UserTokenInterceptor;
import com.xclhove.xnote.interceptor.annotations.UserTokenIntercept;
import com.xclhove.xnote.pojo.form.user.*;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.resolver.annotations.DeviceID;
import com.xclhove.xnote.resolver.annotations.UserInfoFormToken;
import com.xclhove.xnote.service.UserService;
import com.xclhove.xnote.service.VerificationCodeService;
import com.xclhove.xnote.tool.Result;
import com.xclhove.xnote.tool.ThreadLocalTool;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final VerificationCodeService verificationCodeService;
    
    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody UserRegisterForm userRegisterForm) {
        boolean verified = verificationCodeService.verify(userRegisterForm.getVerificationCode(), userRegisterForm.getEmail());
        if (!verified) {
            throw new UserServiceException("验证码错误！");
        }
        
        User user = BeanUtil.copyProperties(userRegisterForm, User.class);
        userService.register(user);
        return Result.success();
    }
    
    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<String> login(
            @Validated @RequestBody UserLoginForm userLoginForm,
            @DeviceID String deviceId
    ) {
        User user = BeanUtil.copyProperties(userLoginForm, User.class);
        
        String token = userService.login(user, deviceId);
        return Result.success(token);
    }
    
    /**
     * 注销
     */
    @UserTokenIntercept
    @PostMapping("/logout")
    public Result<String> logout(
            @DeviceID String deviceId,
            @UserInfoFormToken User user
    ) {
        if (user == null) {
            return Result.success();
        }
        
        userService.logout(user, deviceId);
        return Result.success();
    }
    
    /**
     * 根据id获取用户信息
     */
    @UserTokenIntercept
    @GetMapping("{userId}")
    public Result<User> getById(@PathVariable Integer userId) {
        User user = userService.getByIdWithRedis(userId);
        return Result.success(user);
    }
    
    /**
     * 获取自己的用户信息
     */
    @UserTokenIntercept
    @GetMapping("me")
    public Result<User> getSelfInfo(@UserInfoFormToken User user) {
        return Result.success(user);
    }
    
    /**
     * 刷新token
     */
    @UserTokenIntercept
    @GetMapping("new-token")
    public Result<String> getNewToken(
            @DeviceID String deviceId,
            @UserInfoFormToken User user
    ) {
        String token = userService.login(user, deviceId);
        return Result.success(token);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping
    @UserTokenIntercept
    public Result<?> update(
            @Validated @RequestBody UserUpdateForm userUpdateForm,
            @UserInfoFormToken User user
    ) {
        userService.updateCommonInfo(user, userUpdateForm);
        return Result.success();
    }
    
    /**
     * 更新密码
     */
    @PutMapping("password")
    @UserTokenIntercept
    public Result<?> updatePassword(
            @Validated @RequestBody UserUpdatePasswordForm userUpdatePasswordForm,
            @UserInfoFormToken User user
    ) {
        userService.updatePassword(user, userUpdatePasswordForm);
        return Result.success();
    }
    
    /**
     * 更新邮箱
     */
    @PutMapping("email")
    @UserTokenIntercept
    public Result<?> updateEmail(
            @Validated @RequestBody UserUpdateEmailForm userUpdateEmailForm,
            @UserInfoFormToken User user
    ) {
        boolean verified = verificationCodeService.verify(userUpdateEmailForm.getVerificationCode(), userUpdateEmailForm.getEmail());
        if (!verified) {
            throw new UserServiceException("验证码错误！");
        }
        
        userService.updateEmail(user, userUpdateEmailForm);
        return Result.success();
    }
}
