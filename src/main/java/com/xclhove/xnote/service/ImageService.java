package com.xclhove.xnote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xclhove.xnote.entity.dto.ImagePageDTO;
import com.xclhove.xnote.entity.table.Image;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 图片表 服务类
 * </p>
 *
 * @author xclhove
 * @since 2023-12-09
 */
public interface ImageService extends IService<Image> {
    
    /**
     * 上传图片
     *
     * @param userId    用户id
     * @param imageFile 图片文件流
     * @return 上传后的图片信息
     */
    Image upload(Integer userId, MultipartFile imageFile);
    
    /**
     * 批量删除图片
     *
     * @param userId  用户id，普通用户删除图片时必须传入，管理员删除图片时无需传入
     * @param imageIds 图片id列表
     * @return 删除成功返回true，否则返回false。
     */
    boolean deleteByIds(Integer userId, List<Integer> imageIds);
    
    /**
     * 获取图片信息
     *
     * @param userId  用户id，普通用户删除图片时必须传入，管理员删除图片时无需传入
     * @param imageId 图片id
     * @return 返回图片信息
     */
    Image get(Integer userId, Integer imageId);
    
    /**
     * 分页获取image，设置了userId则获取单个用户的，不设置则获取所有用户的
     *
     * @param pageDTO 分页信息
     * @return 返回分页后的图片信息列表
     */
    ImagePageDTO page(ImagePageDTO pageDTO);
    
    /**
     * 修改图片信息
     *
     * @param image 图片信息，设置了userId则为普通用户修改自己的图片，不设置则为管理员修改任意图片
     * @return 修改成功返回true，否则返回false。
     */
    boolean change(Image image);
    
    /**
     * 下载图片
     *
     * @param response 响应对象
     * @param userId    用户id
     * @param imageId  图片id
     */
    void downloadById(HttpServletResponse response, Integer userId, Integer imageId);
    
    /**
     * 下载图片
     *
     * @param response  响应对象
     * @param imageName 图片名称
     */
    void downloadByName(HttpServletResponse response, String imageName);
    
    /**
     * 下载图片
     * @param response 响应对象
     * @param image 图片信息
     */
    void download(HttpServletResponse response, Image image);
}
