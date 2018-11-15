package com.controller.common.interceptor;

import com.common.Const;
import com.common.ServerResponse;
import com.google.gson.Gson;
import com.pojo.UserInfo;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class AuthorityInterceptor implements HandlerInterceptor {
    @Autowired
    IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o) throws Exception {

        System.out.println("========preHandle========");
        HttpSession session = httpServletRequest.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);

        if (userInfo==null){
            //从cookie中获取token信息
            Cookie[] cookies= httpServletRequest.getCookies();
            if (cookies!=null&&cookies.length>0){
                for (Cookie cookie:cookies){
                    String cookieName=cookie.getName();
                    if (cookieName.equals(Const.AUTOLOGINTOKN)){
                        String autoLoginToken=cookie.getValue();
                        //根据token查询用户信息
                        userInfo=userService.findUserInfoByToken(autoLoginToken);
                        if (userInfo!=null){
                            session.setAttribute(Const.CURRENTUSER,userInfo);
                        }
                        break;
                    }
                }
            }

        }

        //重构HttpServletResponse
        if(userInfo==null||userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter printWriter=response.getWriter();
            if (userInfo==null){
                //未登录
                ServerResponse serverResponse = ServerResponse.serverResponseByError("用户未登录");
                Gson gson=new Gson();
                String json=gson.toJson(serverResponse);
                printWriter.write(json);
            }else {
                ServerResponse serverResponse = ServerResponse.serverResponseByError("无权限操作");
                Gson gson=new Gson();
                String json=gson.toJson(serverResponse);
                printWriter.write(json);
            }
            printWriter.flush();
            printWriter.close();
            return false;
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("========postHandle========");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("========afterCompletion========");
    }


}
