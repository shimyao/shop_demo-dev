package com.controller.backend;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.UserInfo;
import com.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/order/")
public class OrderManageController {
    @Autowired
    IOrderService orderService;

    /**
     * 订单发货
     * */
    @RequestMapping(value = "/send_goods/orderNo/{orderNo}")
    public ServerResponse send_goods(HttpSession session,@PathVariable("orderNo") Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        return orderService.send_goods(userInfo.getId(),orderNo);
    }

}
