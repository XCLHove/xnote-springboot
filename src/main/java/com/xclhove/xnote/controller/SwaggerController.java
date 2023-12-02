package com.xclhove.xnote.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * swagger
 *
 * @author xclhove
 */
@Controller
@RequestMapping("/swagger")
@Slf4j
public class SwaggerController {
    
    @GetMapping()
    public void swaggerDocs(HttpServletRequest request, HttpServletResponse response) {
        String url = "/swagger-ui.html";
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    @GetMapping("/apis")
    public void swaggerApis(HttpServletRequest request, HttpServletResponse response) {
        String url = "/v2/api-docs";
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
}
