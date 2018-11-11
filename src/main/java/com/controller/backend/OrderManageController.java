package com.controller.backend;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.UserInfo;
import com.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/order/")
public class OrderManageController {
    @Autowired
    IOrderService orderService;

    //订单发货
    @RequestMapping(value = "/send_goods.do")
    public ServerResponse send_goods(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        if (userInfo.getRole()==Const.RoleEnum.ROLE_CUSTOMER.getCode()){
            return ServerResponse.serverResponseByError("无权限登录");
        }
        return orderService.send_goods(userInfo.getId(),orderNo);
    }

}
