package com.bjpowernode.crm.settings.web.interceptor;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginInterceptor implements HandlerInterceptor {
    @Override
    //请求到达目标资源之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户是否登陆成功，即查看session中是否有登陆成功时保存的user信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        if (user==null){
            //验证失败，返回登陆页面
            response.sendRedirect(request.getContextPath());
            return false;
        }
        return true;
    }

    @Override
    //执行完目标资源之后
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    //响应返回之后最后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
