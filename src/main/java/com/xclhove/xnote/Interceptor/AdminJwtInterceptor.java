package com.xclhove.xnote.Interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.xclhove.xnote.annotations.AdminJwtIntercept;
import com.xclhove.xnote.entity.table.Admin;
import com.xclhove.xnote.exception.AdminTokenException;
import com.xclhove.xnote.exception.ServiceException;
import com.xclhove.xnote.util.ThreadLocalUtil;
import com.xclhove.xnote.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xclhove
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminJwtInterceptor extends ServiceInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            //如果不是映射到方法直接通过
            if (!(handler instanceof HandlerMethod)) return true;
            
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AdminJwtIntercept loginIntercept = handlerMethod.getMethod().getAnnotation(AdminJwtIntercept.class);
            //如果要访问的方法上没有加这个注解，那么就说明这个方法不需要拦截，否则就需要进行拦截
            if (null == loginIntercept) return true;
            
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) throw new AdminTokenException("管理员未登录！");
            Integer id = TokenUtil.getId(token);
            Admin admin = Db.getById(id, Admin.class);
            String password = admin.getPassword();
            //校验token
            if (!TokenUtil.validate(token, password)) throw new AdminTokenException("token校验未通过！");
            ThreadLocalUtil.set("id", id);
            return true;
        } catch (ServiceException serviceException) {
            throw serviceException;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AdminTokenException("出现异常，token校验未通过！");
        }
    }
}
