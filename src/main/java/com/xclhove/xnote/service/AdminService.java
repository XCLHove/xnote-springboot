package com.xclhove.xnote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xclhove.xnote.entity.table.Admin;

import java.util.List;

/**
 * @author xclhove
 */
public interface AdminService extends IService<Admin> {
    /**
     * 添加管理员
     *
     * @param admin 管理员信息
     * @return 已添加的管理员信息
     */
    public boolean addAdmin(Admin admin);
    
    /**
     * 删除管理员
     *
     * @param id 管理员id
     * @return 已删除的管理员信息
     */
    public boolean deleteAdmin(Integer id);
    
    /**
     * 修改管理员信息
     *
     * @param admin 管理员信息
     * @return 已修改的管理员信息
     */
    public boolean changeAdmin(Admin admin);
    
    /**
     * 获取管理员信息
     *
     * @param id 管理员id
     * @return 管理员信息
     */
    public Admin getAdmin(Integer id);
    
    /**
     * 分页获取所有管理员信息
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 管理员信息集合
     */
    public List<Admin> getAdmins(Integer pageNumber, Integer pageSize);
}
