package com.xclhove.xnote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.util.Result;

/**
 * @author xclhove
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功后用户信息
     */
    Result<User> register(User user);
    
    /**
     * 以后注册
     * @param account 账号
     * @param password 密码
     * @return token
     */
    Result<String> login(String account, String password);
    
    /**
     * 用户修改信息
     * @param user 用户信息
     * @return 用户更新成功后的信息
     */
    Result<User> updateInfo(User user);
    
    /**
     * 通过用户id查询用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    Result<User> queryUserInfoById(Integer userId);
    
    /**
     * 禁封用户
     * @param userId 要禁封的用户
     * @return 用户信息
     */
    Result<User> ban(Integer userId);
}
