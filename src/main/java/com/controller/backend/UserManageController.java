package com.controller.backend;

import com.Utils.IpUtils;
import com.Utils.MD5Utils;
import com.common.Const;
import com.common.ServerResponse;
import com.pojo.UserInfo;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.SocketException;
import java.net.UnknownHostException;

//后台用户控制器类
@RestController
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    IUserService userService;

    /**
     * 管理员登录
     * */
    @RequestMapping(value = "/login/{username}/{password}")
    public ServerResponse login(HttpServletResponse response,HttpServletRequest request, HttpSession session,
                                @PathVariable("username") String username,
                                @PathVariable("password") String password){
        ServerResponse serverResponse = userService.login(username,password);
        if (serverResponse.isSuccess()){
            //登录成功
           UserInfo userInfo = (UserInfo) serverResponse.getData();
          if (userInfo.getRole()==Const.RoleEnum.ROLE_CUSTOMER.getCode()){
                return ServerResponse.serverResponseByError("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
          //生成autoLoginToken
           String ip= IpUtils.getRemoteAddress(request);
            try {
                String mac= IpUtils.getMACAddress(ip);
                String token= MD5Utils.getMD5Code(mac);
                //token保存到数据库
                 userService.updateTokenByUserId(userInfo.getId(),token);
                 //token作为cookie响应到客户端
                Cookie autoLoginTokenCookie=new Cookie(Const.AUTOLOGINTOKN,token);
                autoLoginTokenCookie.setMaxAge(7*24*60*60);//7天
                autoLoginTokenCookie.setPath("/");
                response.addCookie(autoLoginTokenCookie);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return serverResponse;
    }

}
