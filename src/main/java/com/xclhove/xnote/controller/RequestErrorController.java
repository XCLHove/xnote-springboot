package com.xclhove.xnote.controller;

import com.xclhove.xnote.exception.NotFoundException;
import com.xclhove.xnote.tool.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求错误相关接口
 *
 * @author xclhove
 */
@Controller
@Slf4j
@Order
public class RequestErrorController {
    
    /**
     * 请求路径不存在
     */
    @RequestMapping(value = "/**")
    public Result<?> notFound(HttpServletRequest request) {
        String message = String.format("请求路径不存在：%s", request.getRequestURI());
        log.error(message);
        throw new NotFoundException(message);
    }
}
