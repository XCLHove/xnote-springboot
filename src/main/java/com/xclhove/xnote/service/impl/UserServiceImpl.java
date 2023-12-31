package com.xclhove.xnote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.enums.entityattribute.UserStatus;
import com.xclhove.xnote.exception.UserServiceException;
import com.xclhove.xnote.exception.UserTokenException;
import com.xclhove.xnote.mapper.UserMapper;
import com.xclhove.xnote.service.UserService;
import com.xclhove.xnote.tool.EmailTool;
import com.xclhove.xnote.tool.RedisTool;
import com.xclhove.xnote.util.EncryptUtil;
import com.xclhove.xnote.util.TokenUtil;
import com.xclhove.xnote.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final String regex = "^[\\w\\.\\*_]{5,30}$";
    private final EmailTool emailTool;
    private final RedisTool redisTool;
    
    @Override
    public boolean register(User user) {
        boolean saveSuccess = false;
        try {
            String account = user.getAccount();
            String password = user.getPassword();
            if (!account.matches(regex)) throw new UserServiceException("账号格式不正确！");
            if (!password.matches(regex)) throw new UserServiceException("密码格式不正确！");
            String encryptedPassword = EncryptUtil.encrypt(password, account, EncryptUtil.EncryptionAlgorithm.SHA256);
            user.setPassword(encryptedPassword);
            saveSuccess = this.save(user);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new UserServiceException("账号已存在！");
        }
        if (!saveSuccess) throw new UserServiceException("注册失败，请重新注册!");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, user.getAccount());
        return true;
    }
    
    @Override
    public String login(String account, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, account);
        if (this.getOne(queryWrapper) == null) throw new UserServiceException("账号不存在！");
        String encryptPassword = EncryptUtil.encrypt(password, account, EncryptUtil.EncryptionAlgorithm.SHA256);
        queryWrapper.eq(User::getPassword, encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) throw new UserServiceException("密码错误！");
        String token = TokenUtil.generate(user.getId(), user.getPassword());
        return token;
    }
    
    @Override
    public boolean updateInfo(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, user.getId());
        updateWrapper.set(!StrUtil.isBlank(user.getName()), User::getName, user.getName());
        updateWrapper.set(!StrUtil.isBlank(user.getEmail()), User::getEmail, user.getEmail());
        
        String password = user.getPassword();
        if ((!StrUtil.isBlank(password)) && (!password.matches(regex))) throw new UserServiceException("密码格式不正确！");
        updateWrapper.set(!StrUtil.isBlank(password),
                User::getPassword,
                EncryptUtil.encrypt(password, user.getAccount(), EncryptUtil.EncryptionAlgorithm.SHA256)
        );
        
        boolean updateSuccess = false;
        try {
            updateSuccess = this.update(updateWrapper);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new UserServiceException("更新用户信息失败！");
        }
        if (!updateSuccess) throw new UserServiceException("更新用户信息失败！");
        return true;
    }
    
    @Override
    public User queryById(Integer userId) {
        if (!(userId > 0)) throw new UserServiceException("用户id错误!");
        User user = this.getById(userId);
        if (user == null) throw new UserServiceException("用户不存在！");
        return user;
    }
    
    @Override
    public boolean banById(Integer userId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                .set(User::getStatus, UserStatus.BANED);
        boolean banSuccess = false;
        try {
            banSuccess = this.update(updateWrapper);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserTokenException("出现异常，禁封失败！");
        }
        if (!banSuccess) throw new UserTokenException("禁封失败！");
        return true;
    }
    
    @Override
    public void sendVerificationCode(String email) {
        int expirationDate = 1;
        String verificationCode = VerificationCodeUtil.generateVerificationCode(4);
        String generateVerificationCodeKey = VerificationCodeUtil.generateVerificationCodeKey(email);
        redisTool.setValue(generateVerificationCodeKey, verificationCode, expirationDate, TimeUnit.MINUTES);
        
        String subject = "XNote验证码";
        String content = "XNote验证码：" + verificationCode + " ，有效期：" + expirationDate + "分钟。";
        try {
            emailTool.sendMail(email, subject, content);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserServiceException("验证码发送失败！");
        }
    }
    
    @Override
    public boolean verifyVerificationCode(String email, String verificationCode) {
        String verificationCodeKey = VerificationCodeUtil.generateVerificationCodeKey(email);
        String code = redisTool.getValue(verificationCodeKey, String.class);
        if (code == null) {
            return false;
        }
        if (!code.equalsIgnoreCase(verificationCode)) {
            return false;
        }
        redisTool.deleteValue(verificationCodeKey);
        return true;
    }
}
