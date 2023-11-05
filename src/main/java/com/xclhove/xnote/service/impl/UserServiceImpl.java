package com.xclhove.xnote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.enums.ResultStatus;
import com.xclhove.xnote.enums.UserStatus;
import com.xclhove.xnote.mapper.UserMapper;
import com.xclhove.xnote.service.UserService;
import com.xclhove.xnote.util.EncryptUtil;
import com.xclhove.xnote.util.Result;
import com.xclhove.xnote.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author xclhove
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final String regex = "^[\\w\\.\\*]{6,30}$";
    
    @Override
    public Result<User> register(User user) {
        boolean saveSuccess = false;
        try {
            String account = user.getAccount();
            String password = user.getPassword();
            if (!account.matches(regex)) {
                return Result.error("账号格式不正确！");
            }
            if (!password.matches(regex)) {
                return Result.error("密码格式不正确！");
            }
            String encryptedPassword = EncryptUtil.encrypt(password, account, EncryptUtil.EncryptionAlgorithm.SHA256);
            user.setPassword(encryptedPassword);
            saveSuccess = this.save(user);
        } catch (DuplicateKeyException duplicateKeyException) {
            return Result.error("账号已存在！");
        } catch (Exception exception) {
            log.error(exception.toString());
            return Result.error("注册失败，请重新注册!");
        }
        if (!saveSuccess) {
            return Result.error("注册失败，请重新注册!");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, user.getAccount());
        return Result.success("注册成功！", user);
    }
    
    @Override
    public Result<String> login(String account, String password) {
        if (!account.matches(regex)) {
            return Result.error("账号格式不正确！");
        }
        if (!password.matches(regex)) {
            return Result.error("密码格式不正确！");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, account);
        if (this.list(queryWrapper).isEmpty()) {
            return Result.error("账号不存在！");
        }
        queryWrapper.eq(User::getPassword, EncryptUtil.encrypt(password, account, EncryptUtil.EncryptionAlgorithm.SHA256));
        User user = this.getOne(queryWrapper);
        if (user == null) {
            return Result.error("密码错误！");
        }
        String token = TokenUtil.generate(user.getId(), user.getPassword());
        return Result.success("登录成功！", token);
    }
    
    @Override
    public Result<User> updateInfo(User user) {
        String userName = user.getName();
        String password = user.getPassword();
        if (!password.matches(regex)) {
            return Result.error("密码格式不正确！");
        }
        String email = user.getEmail();
        User newUser = this.getById(user.getId());
        if (!StrUtil.isBlank(userName)) {
            newUser.setName(userName);
        }
        if (!StrUtil.isBlank(password)) {
            password = EncryptUtil.encrypt(password, user.getAccount(), EncryptUtil.EncryptionAlgorithm.SHA256);
            newUser.setPassword(password);
        }
        if (!StrUtil.isBlank(email)) {
            newUser.setEmail(email);
        }
        boolean updateSuccess = false;
        try {
            updateSuccess = this.updateById(newUser);
        } catch (Exception exception) {
            log.error(exception.toString());
            return Result.error(ResultStatus.ERROR, "更新用户信息失败！", user);
        }
        if (!updateSuccess) {
            return Result.error(ResultStatus.ERROR, "更新用户信息失败！", user);
        }
        newUser = this.getById(user.getId());
        return Result.success("更新用户信息成功！", newUser);
    }
    
    @Override
    public Result<User> queryUserInfoById(Integer userId) {
        if (!(userId > 0)) {
            return Result.error("用户id错误!");
        }
        User user = this.getById(userId);
        if (user == null) {
            return Result.error("没有该用户！");
        }
        return Result.success("查询用户信息成功！", user);
    }
    
    @Override
    public Result<User> ban(Integer userId) {
        if (!(userId > 0)) {
            return Result.error("用户id错误!");
        }
        User user = this.getById(userId);
        if (user == null) {
            return Result.error("没有该用户！");
        }
        user.setStatus(UserStatus.BANED);
        boolean banSuccess = false;
        try {
            banSuccess = this.updateById(user);
            if (banSuccess) {
                return Result.success("禁封成功！",user);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return Result.error("禁封失败！");
    }
    
}
