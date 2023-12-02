package com.xclhove.xnote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.table.Admin;
import com.xclhove.xnote.exception.AdminServiceException;
import com.xclhove.xnote.mapper.AdminMapper;
import com.xclhove.xnote.service.AdminService;
import com.xclhove.xnote.util.EncryptUtil;
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
    public boolean addAdmin(Admin admin) {
        String password = admin.getPassword();
        password = EncryptUtil.encrypt(password, admin.getAccount(), EncryptUtil.EncryptionAlgorithm.SHA256);
        admin.setPassword(password);
        boolean addSuccess = false;
        try {
            addSuccess = this.save(admin);
        } catch (Exception e) {
            log.error(e.toString());
            throw new AdminServiceException("出现异常，添加管理员失败！");
        }
        if (!addSuccess) throw new AdminServiceException("添加管理员失败！");
        return true;
    }
    
    @Override
    public boolean deleteAdmin(Integer id) {
        return true;
    }
    
    @Override
    public boolean changeAdmin(Admin admin) {
        return true;
    }
    
    @Override
    public Admin getAdmin(Integer id) {
        return null;
    }
    
    @Override
    public List<Admin> getAdmins(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
