package com.xclhove.xnote.util;

import com.xclhove.xnote.enums.ResultMessage;
import com.xclhove.xnote.enums.ResultStatus;
import com.xclhove.xnote.enums.ResultType;
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
    private ResultStatus status;
    @ApiModelProperty(value = "类型", example = "success")
    private ResultType type;
    @ApiModelProperty(value = "信息", example = "请求成功！")
    private String message;
    @ApiModelProperty(value = "数据")
    private T data;
    
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultStatus.SUCCESS, ResultType.SUCCESS, ResultMessage.SUCCESS.getMessage(), data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(ResultStatus.SUCCESS, ResultType.SUCCESS, message, data);
    }
    
    public static <T> Result<T> success(ResultStatus status, String message, T data) {
        return new Result<T>(status, ResultType.SUCCESS, message, data);
    }
    
    public static <Object> Result<Object> error(String message) {
        return new Result<>(ResultStatus.ERROR, ResultType.ERROR, message, null);
    }
    
    public static <Object> Result<Object> error(ResultStatus status, String message) {
        return new Result<>(status, ResultType.ERROR, message, null);
    }
    
    public static <T> Result<T> error(ResultStatus status, String message, T data) {
        return new Result<>(status, ResultType.ERROR, message, data);
    }
}
