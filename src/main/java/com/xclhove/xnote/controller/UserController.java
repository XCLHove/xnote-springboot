package com.xclhove.xnote.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xclhove.xnote.annotations.AdminJwtIntercept;
import com.xclhove.xnote.annotations.UserJwtIntercept;
import com.xclhove.xnote.entity.dto.UserDTO;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.service.UserService;
import com.xclhove.xnote.util.Result;
import com.xclhove.xnote.util.ThreadLocalUtil;
import com.xclhove.xnote.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xclhove
 */
@RestController
@RequestMapping("/users")
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/{userId}")
    @ApiOperation(value = "查询用户信息")
    public Result<User> queryUserById(@PathVariable
                                      @ApiParam(value = "用户id", example = "1")
                                      Integer userId) {
        User user = userService.queryById(userId);
        return Result.success(user);
    }
    
    @GetMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<String> login(@RequestParam
                                @ApiParam(value = "账号", example = "user123456")
                                String account,
                                @RequestParam
                                @ApiParam(value = "密码", example = "123456")
                                String password) {
        String token = userService.login(account, password);
        return Result.success(token);
    }
    
    @GetMapping("/self")
    @UserJwtIntercept
    @ApiOperation(value = "查询用户自己的信息")
    public Result<User> querySelfInfo() {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        User user = userService.queryById(userId);
        return Result.success(user);
    }
    
    @PutMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result<Object> register(@RequestBody
                                 @ApiParam(value = "用户信息")
                                 UserDTO userDTO) {
        User user = BeanUtil.copyProperties(userDTO, User.class);
        userService.register(user);
        return Result.success();
    }
    
    @PostMapping
    @UserJwtIntercept
    @ApiOperation(value = "更新用户信息")
    public Result<User> update(HttpServletRequest request,
                               @RequestBody
                               @ApiParam(value = "用户信息")
                               UserDTO userDTO) {
        String token = request.getHeader("token");
        Integer userId = TokenUtil.getId(token);
        User user = BeanUtil.copyProperties(userDTO, User.class);
        user.setId(userId);
        userService.updateInfo(user);
        return Result.success();
    }
    
    @PostMapping("/ban/{userId}")
    @AdminJwtIntercept
    @ApiOperation("禁封用户")
    public Result<User> ban(@PathVariable
                            @ApiParam(value = "用户id", example = "1")
                            Integer userId) {
        userService.banById(userId);
        return Result.success();
    }
}
