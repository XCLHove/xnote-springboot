package com.xclhove.xnote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xclhove.xnote.entity.table.User;

/**
 * @author xclhove
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param user User对象
     * @return 是否注册成功
     */
    boolean register(User user);
    
    /**
     * 用户注册
     *
     * @param account  账号
     * @param password 密码
     * @return token字符串
     */
    String login(String account, String password);
    
    /**
     * 用户修改信息
     *
     * @param user User对象
     * @return 是否更新成功
     */
    boolean updateInfo(User user);
    
    /**
     * 通过用户id查询用户信息
     *
     * @param userId 用户id
     * @return User对象
     */
    User queryById(Integer userId);
    
    /**
     * 通过用户id禁封用户
     *
     * @param userId 用户id
     * @return 是否禁封成功
     */
    boolean banById(Integer userId);
}
