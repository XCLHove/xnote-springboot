package com.xclhove.xnote.util;

import com.xclhove.xnote.enums.result.ResultType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "结果")
public class Result<T> {
    @ApiModelProperty(value = "状态码", example = "200")
    private int status;
    @ApiModelProperty(value = "信息", example = "请求成功！")
    private String message;
    @ApiModelProperty(value = "数据")
    private T data;
    
    public static <T> Result<T> success() {
        return new Result<>(ResultType.SUCCESS.getStatus(), ResultType.SUCCESS.getMessage(), null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultType.SUCCESS.getStatus(), ResultType.SUCCESS.getMessage(), data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultType.SUCCESS.getStatus(), message, data);
    }
    
    public static <T> Result<T> success(int status, String message, T data) {
        return new Result<>(status, message, data);
    }
    
    public static <T> Result<T> error() {
        return new Result<>(ResultType.ERROR.getStatus(), ResultType.ERROR.getMessage(), null);
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(ResultType.ERROR.getStatus(), message, null);
    }
    
    public static <T> Result<T> error(int status, String message) {
        return new Result<>(status, message, null);
    }
    
    public static <T> Result<T> error(int status, String message, T data) {
        return new Result<>(status, message, data);
    }
}
