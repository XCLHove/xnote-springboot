package com.xclhove.xnote.controller;

import com.xclhove.xnote.exception.NotFoundException;
import io.swagger.annotations.Api;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请求错误相关接口
 *
 * @author xclhove
 */
@Controller
@Api(tags = "请求错误相关接口")
@Order()
public class RequestErrorController {
    
    /**
     * 请求路径不存在
     */
    @RequestMapping(value = "/**")
    public String notFound() {
        throw new NotFoundException();
    }
}
