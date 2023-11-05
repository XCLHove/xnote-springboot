package com.xclhove.xnote.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xclhove.xnote.annotations.AdminJwtIntercept;
import com.xclhove.xnote.annotations.UserJwtIntercept;
import com.xclhove.xnote.annotations.UserStatusIntercept;
import com.xclhove.xnote.entity.dto.UserDTO;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.service.UserService;
import com.xclhove.xnote.util.Result;
import com.xclhove.xnote.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xclhove
 */
@RestController
@RequestMapping("/users")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/{userId}")
    @ApiOperation(value = "查询用户信息")
    public Result<User> queryUserInfo(@PathVariable
                                      @ApiParam(value = "用户id", example = "1")
                                      Integer userId) {
        return userService.queryUserInfoById(userId);
    }
    
    @GetMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<String> login(@RequestParam
                                @ApiParam(value = "账号", example = "user123456")
                                String account,
                                @RequestParam
                                @ApiParam(value = "密码", example = "123456")
                                String password) {
        return userService.login(account, password);
    }
    
    @GetMapping("/self")
    @UserJwtIntercept
    @ApiOperation(value = "查询用户自己的信息")
    public Result<User> querySelfInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        int userId = TokenUtil.getId(token);
        return userService.queryUserInfoById(userId);
    }
    
    @PutMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result<User> register(@RequestBody
                                 @ApiParam(value = "用户信息")
                                 UserDTO userDTO) {
        User user = BeanUtil.copyProperties(userDTO, User.class);
        return userService.register(user);
    }
    
    @PostMapping
    @UserJwtIntercept
    @UserStatusIntercept
    @ApiOperation(value = "更新用户信息")
    public Result<User> update(HttpServletRequest request,
                               @RequestBody
                               @ApiParam(value = "用户信息")
                               UserDTO userDTO) {
        String token = request.getHeader("token");
        Integer userId = TokenUtil.getId(token);
        User user = BeanUtil.copyProperties(userDTO, User.class);
        user.setId(userId);
        return userService.updateInfo(user);
    }
    
    @PostMapping("/ban/{userId}")
    @AdminJwtIntercept
    @ApiOperation("禁封用户")
    public Result<User> ban(@PathVariable
                            @ApiParam(value = "用户id", example = "1")
                            Integer userId) {
        return userService.ban(userId);
    }
}
