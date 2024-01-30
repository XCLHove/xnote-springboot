package com.xclhove.xnote.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xclhove.xnote.Interceptor.AdminJwtInterceptor;
import com.xclhove.xnote.Interceptor.UserJwtInterceptor;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.constant.RequestHeaderKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.entity.dto.UserDTO;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.exception.OperationFrequencyException;
import com.xclhove.xnote.exception.VerificationCodeException;
import com.xclhove.xnote.service.UserService;
import com.xclhove.xnote.tool.RedisTool;
import com.xclhove.xnote.util.Result;
import com.xclhove.xnote.util.ThreadLocalUtil;
import com.xclhove.xnote.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@RestController
@RequestMapping("/users")
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final RedisTool redisTool;
    
    @GetMapping("/{userId}")
    @ApiOperation(value = "查询用户信息")
    public Result<User> queryUserById(@PathVariable
                                      @ApiParam(value = "用户id", example = "1")
                                      @Pattern(regexp = "^\\d+$", message = "用户id必须为数字！")
                                      Integer userId) {
        User user = userService.queryById(userId);
        return Result.success(user);
    }
    
    @GetMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<String> login(@RequestParam
                                @ApiParam(value = "账号", example = "user123456")
                                @Pattern(regexp = "^[a-zA-Z0-9_.*]{5,30}$", message = "账号仅支持5到30位的数字、字母、‘_’、‘.’和‘*’！")
                                String account,
                                @RequestParam
                                @ApiParam(value = "密码", example = "123456")
                                @Pattern(regexp = "^[a-zA-Z0-9_.*]{5,30}$", message = "账号仅支持5到30位的数字、字母、‘_’、‘.’和‘*’！")
                                String password) {
        String token = userService.login(account, password);
        return Result.success("登录成功！", token);
    }
    
    @PostMapping("/logout")
    @UserJwtInterceptor.UserJwtIntercept
    @ApiOperation(value = "用户登出")
    public Result<Object> logout(@RequestHeader(value = RequestHeaderKey.TOKEN) String token) {
        Integer userId = TokenUtil.getId(token);
        userService.logout(userId);
        return Result.success("登出成功！", null);
    }
    
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return this.login(userDTO.getAccount(), userDTO.getPassword());
    }
    
    @GetMapping("/self")
    @UserJwtInterceptor.UserJwtIntercept
    @ApiOperation(value = "查询用户自己的信息")
    public Result<User> querySelfInfo() {
        Integer userId = ThreadLocalUtil.get(TreadLocalKey.ID, Integer.class);
        User user = userService.queryById(userId);
        return Result.success(user);
    }
    
    @PutMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result<Object> register(@RequestBody
                                   @ApiParam(value = "用户信息")
                                   UserDTO userDTO) {
        boolean verifiedPassed = userService.verifyVerificationCode(userDTO.getEmail(), userDTO.getVerificationCode());
        if (!verifiedPassed) throw new VerificationCodeException("验证码错误！");
        User user = BeanUtil.copyProperties(userDTO, User.class);
        userService.register(user);
        return Result.success("注册成功！", null);
    }
    
    @PostMapping
    @UserJwtInterceptor.UserJwtIntercept
    @ApiOperation(value = "更新用户信息")
    public Result<User> update(HttpServletRequest request,
                               @RequestBody
                               @ApiParam(value = "用户信息")
                               UserDTO userDTO) {
        String token = request.getHeader(RequestHeaderKey.TOKEN);
        Integer userId = TokenUtil.getId(token);
        User user = BeanUtil.copyProperties(userDTO, User.class);
        user.setId(userId);
        userService.updateInfo(user);
        return Result.success();
    }
    
    @PostMapping("/ban/{userId}")
    @AdminJwtInterceptor.AdminJwtIntercept
    @ApiOperation("禁封用户")
    public Result<User> ban(@PathVariable
                            @ApiParam(value = "用户id", example = "1")
                            Integer userId) {
        userService.banById(userId);
        return Result.success();
    }
    
    @GetMapping("/verificationCode")
    @ApiOperation("发送验证码")
    public Result<Object> sendVerificationCode(HttpServletRequest request,
                                               @RequestParam
                                               @ApiParam(value = "邮箱")
                                               String email) {
        String ip = request.getRemoteAddr();
        String redisKey = RedisKey.VERIFICATION_CODE_IP_LIMIT + ip;
        Integer value = redisTool.getValue(redisKey, Integer.class);
        
        final int maxFrequencyPerMinute = 2;
        int frequency = (value == null) ? 0 : value;
        if (frequency >= maxFrequencyPerMinute) throw new OperationFrequencyException();
        
        userService.sendVerificationCode(email);
        redisTool.setValue(redisKey, ++frequency, 1, TimeUnit.MINUTES);
        return Result.success("验证码发送成功！", null);
    }
}
