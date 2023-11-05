package com.xclhove.xnote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.table.Admin;
import com.xclhove.xnote.mapper.AdminMapper;
import com.xclhove.xnote.service.AdminService;
import com.xclhove.xnote.util.EncryptUtil;
import com.xclhove.xnote.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xclhove
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public Result<Admin> addAdmin(Admin admin) {
        try {
            String password = admin.getPassword();
            password = EncryptUtil.encrypt(password, admin.getAccount(), EncryptUtil.EncryptionAlgorithm.SHA256);
            admin.setPassword(password);
            boolean addSuccess = this.save(admin);
            if (!addSuccess) {
                return Result.error("添加管理员失败！");
            }
            LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
            adminLambdaQueryWrapper.eq(Admin::getAccount, admin.getAccount());
            admin = this.getOne(adminLambdaQueryWrapper);
            return Result.success(admin);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error("添加管理员失败！");
        }
    }
    
    @Override
    public Result<Admin> deleteAdmin(Integer id) {
        return null;
    }
    
    @Override
    public Result<Admin> changeAdmin(Admin admin) {
        return null;
    }
    
    @Override
    public Result<Admin> getAdmin(Integer id) {
        return null;
    }
    
    @Override
    public Result<List<Admin>> getAdmins(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
