package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserServices;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Resource
    private UserServices userServices;


    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletResponse response, HttpServletRequest request, HttpSession session){
        //封装参数（map）
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service方法
        User user = userServices.queryUserByLoginActAndPwd(map);
        //根据查询结果，生成响应信息
        ReturnObject returnObject = new ReturnObject();
        if (user==null){
            //用户名密码错误登陆失败
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误!");
        }else {
            //用户名密码存在，进一步判断账户是否合法
            String date = DataUtils.formatDataTime(new Date());
            if (DataUtils.formatDataTime(new Date()).compareTo(user.getExpiretime()) > 0) {
                //账号过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户已过期!");
            } else if ("0".equals(user.getLockstate())) {
                //账号被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户已锁定!");
            } else if (!user.getAllowips().contains(request.getRemoteAddr())) {
                //登陆失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("IP地址受限!+"+request.getRemoteAddr());
            } else {
                //登陆成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                session.setAttribute(Contants.SESSION_USER,user);
                //如果需要记住密码，往外写cookie
                if ("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct",user.getLoginact());
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd",user.getLoginpwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else {
                    //清除未过期的cookie
                    Cookie c1 = new Cookie("loginAct","1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd","1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清除cookie
        Cookie c1 = new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();
        //跳转到首页
       return "redirect:/";
    }
}
