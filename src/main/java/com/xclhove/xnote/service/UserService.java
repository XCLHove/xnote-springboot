package com.xclhove.xnote.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.UserServiceException;
import com.xclhove.xnote.mapper.UserMapper;
import com.xclhove.xnote.pojo.dto.UserDTO;
import com.xclhove.xnote.pojo.enums.UserStatus;
import com.xclhove.xnote.pojo.form.user.UserUpdateEmailForm;
import com.xclhove.xnote.pojo.form.user.UserUpdateForm;
import com.xclhove.xnote.pojo.form.user.UserUpdatePasswordForm;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.tool.RedisTool;
import com.xclhove.xnote.tool.UserTokenTool;
import com.xclhove.xnote.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTool redisTool;
    
    private User getWithRedis(String redisKey, RedisTool.Getter<User> getter) {
        return redisTool.getUseStringAntiCachePassThrough(
                redisKey,
                getter,
                user -> {
                    UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
                    return JSON.toJSONString(userDTO);
                },
                jsonString -> {
                    UserDTO userDTO = JSON.parseObject(jsonString, UserDTO.class);
                    return BeanUtil.copyProperties(userDTO, User.class);
                },
                30,
                TimeUnit.MINUTES
        );
    }
    
    public String getTokenRedisKey(int userId, String deviceId) {
        return RedisKey.join(RedisKey.User.TOKEN, String.valueOf(userId), deviceId);
    }
    
    private String getRedisKeyById(int userId) {
        return RedisKey.join(RedisKey.User.ID, String.valueOf(userId));
    }
    
    private String getRedisKeyByAccount(String account) {
        return RedisKey.join(RedisKey.User.ACCOUNT, account);
    }
    
    private String getRedisKeyByEmail(String email) {
        return RedisKey.join(RedisKey.User.EMAIL, email);
    }
    
    /**
     * 通过账号获取用户信息
     */
    public User getByAccount(String account) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, account);
        return this.getOne(queryWrapper);
    }
    
    /**
     * 通过邮箱获取用户信息
     */
    public User getByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return this.getOne(queryWrapper);
    }
    
    /**
     * 通过ID获取用户信息（使用redis做缓存，有防缓存穿透）
     */
    public User getByIdWithRedis(int userId) {
        return getWithRedis(getRedisKeyById(userId), () -> getById(userId));
    }
    
    /**
     * 通过账号获取用户信息（使用redis做缓存，有防缓存穿透）
     */
    public User getByAccountWithRedis(String account) {
        return getWithRedis(getRedisKeyByAccount(account), () -> getByAccount(account));
    }
    
    /**
     * 通过邮箱获取用户信息（使用redis做缓存，有防缓存穿透）
     */
    public User getByEmailWithRedis(String email) {
        return getWithRedis(getRedisKeyByEmail(email), () -> getByEmail(email));
    }
    
    /**
     * 密码加密
     * @param account 账号
     * @param password 未加密的密码
     * @return 加密后的密码
     */
    private String encryptPassword(String account, String password) {
        return EncryptUtil.encrypt(password, account, EncryptUtil.EncryptionAlgorithm.SHA256);
    }
    
    /**
     * 注册
     */
    public void register(User user) {
        //判断账号是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, user.getAccount());
        if (this.count(queryWrapper) > 0) {
            throw new UserServiceException("账号已存在");
        }
        
        // 判断邮箱是否已注册
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());
        if (this.count(queryWrapper) > 0) {
            throw new UserServiceException("邮箱已被注册");
        }
        
        // 密码加密
        String encryptPassword = encryptPassword(user.getAccount(), user.getPassword());
        user.setPassword(encryptPassword);
        
        user.setId(null);
        user.setStatus(UserStatus.ENABLE);
        user.setHomePageNoteId(null);
        boolean success = save(user);
        if (!success) {
            throw new UserServiceException("系统异常，注册失败");
        }
        
        stringRedisTemplate.delete(getRedisKeyByAccount(user.getAccount()));
        stringRedisTemplate.delete(getRedisKeyByEmail(user.getEmail()));
    }
    
    /**
     * 登录
     */
    public String login(User user, String deviceId) {
        User userInDatabase = this.getByAccountWithRedis(user.getAccount());
        if (userInDatabase == null) {
            throw new UserServiceException("账号不存在");
        }
        
        String encryptPassword = encryptPassword(user.getAccount(), user.getPassword());
        if (!userInDatabase.getPassword().equals(encryptPassword)) {
            throw new UserServiceException("密码错误");
        }
        
        String redisKey = getTokenRedisKey(userInDatabase.getId(), deviceId);
        String tokenInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isNotBlank(tokenInRedis)) {
            return tokenInRedis;
        }
        
        String newToken = UserTokenTool.generate(encryptPassword, userInDatabase.getId());
        stringRedisTemplate.opsForValue().set(redisKey, newToken, 2, TimeUnit.HOURS);
        return newToken;
    }
    
    /**
     * 注销
     */
    public void logout(User user, String deviceId) {
        String redisKey = getTokenRedisKey(user.getId(), deviceId);
        Boolean success = stringRedisTemplate.delete(redisKey);
        
        if (success == null || !success) {
            throw new UserServiceException("登出失败");
        }
    }
    
    /**
     * 更新用户信息（使用redis）
     */
    private boolean updateByIdWithRedis(User user) {
        boolean success = updateById(user);
        if (!success) {
            return false;
        }
        
        // redis
        User userInRedis = getByIdWithRedis(user.getId());
        stringRedisTemplate.delete(getRedisKeyById(userInRedis.getId()));
        stringRedisTemplate.delete(getRedisKeyByAccount(userInRedis.getAccount()));
        
        return true;
    }
    
    /**
     * 更新用户信息，不包含密码和邮箱
     */
    public void updateCommonInfo(User user, UserUpdateForm userUpdateForm) {
        User newUser = BeanUtil.copyProperties(userUpdateForm, User.class);
        newUser.setId(user.getId());
        
        boolean success = updateByIdWithRedis(newUser);
        if (!success) {
            throw new UserServiceException();
        }
    }
    
    /**
     * 更新密码
     */
    public void updatePassword(User user, UserUpdatePasswordForm userUpdatePasswordForm) {
        User newUser = new User();
        newUser.setId(user.getId());
        String encryptPassword = encryptPassword(user.getAccount(), userUpdatePasswordForm.getPassword());
        newUser.setPassword(encryptPassword);
        boolean success = updateByIdWithRedis(user);
        if (!success) {
            throw new UserServiceException();
        }
        
        String redisKey = getTokenRedisKey(user.getId(), "*");
        Set<String> keys = stringRedisTemplate.keys(redisKey);
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }
    
    /**
     * 更新邮箱
     */
    public void updateEmail(User user, UserUpdateEmailForm userUpdateEmailForm) {
        String newEmail = userUpdateEmailForm.getEmail();
        if(user.getEmail().equals(newEmail)) {
            return;
        }
        
        User newEmailOwner = getByEmailWithRedis(newEmail);
        if (newEmailOwner != null) {
            throw new UserServiceException("邮箱已绑定，请先换绑");
        }
        
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setEmail(newEmail);
        
        boolean success = updateByIdWithRedis(newUser);
        if (!success) {
            throw new UserServiceException();
        }
        
        stringRedisTemplate.delete(getRedisKeyByEmail(newEmail));
    }
}
