package com.xclhove.xnote.entity.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * <p>
 * 图片表
 * </p>
 *
 * @author xclhove
 * @since 2023-12-09
 */
@Data
@TableName("image")
@ApiModel(value="Image对象", description="图片表")
public class Image {

    @ApiModelProperty(value = "图片id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "图片别名")
    private String alias;

    @ApiModelProperty(value = "图片名称")
    private String name;

    @ApiModelProperty(value = "上一次下载的时间")
    private Timestamp lastDownloadTime;
}
