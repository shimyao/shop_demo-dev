package com.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.common.Const;
import com.common.ServerResponse;
import com.google.common.collect.Maps;
import com.pojo.UserInfo;
import com.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    IOrderService orderService;

    /**
     * 创建订单
     * */
    @RequestMapping(value = "/create/shippingId/{shippingId}")
    public ServerResponse createOrder(HttpSession session,
                                      @PathVariable("shippingId") Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return orderService.createOrder(userInfo.getId(),shippingId);
    }

    /**
     * 取消订单
     * */
    @RequestMapping(value = "/cancel/orderNo/{orderNo}")
    public ServerResponse cancel(HttpSession session,
                                 @PathVariable("orderNo") Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return orderService.cancel(userInfo.getId(),orderNo);
    }

    /**
     * 获取订单的商品信息(购物车中)
     * */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return orderService.get_order_cart_product(userInfo.getId());
    }

    /**
     * 订单列表list
     * */
    @RequestMapping(value = "/list/{pageNum}/{pageSize}")
    public ServerResponse list(HttpSession session,
                               @PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return orderService.list(userInfo.getId(),pageNum,pageSize);
    }

    /**
     * 订单详情 detail
     * */
    @RequestMapping(value = "/detail/orderNo/{orderNo}")
    public ServerResponse detail(HttpSession session,
                                 @PathVariable("orderNo") Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return orderService.detail(userInfo.getId(),orderNo);
    }

    /**
     * 支付接口
     * */
    @RequestMapping(value = "/pay/orderNo/{orderNo}")
    public ServerResponse pay(HttpSession session,
                              @PathVariable("orderNo") Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }

        return orderService.pay(userInfo.getId(),orderNo);
    }

    /**
     * 支付宝服务器回调应用服务器接口
     * */
    @RequestMapping(value = "/alipay_callback.do")
    public ServerResponse alipay_callback(HttpServletRequest request){
        System.out.println("====支付宝服务器回调应用服务器接口====");

        Map<String,String[]> params=request.getParameterMap();
        Map<String,String> requestparams= Maps.newHashMap();
        Iterator<String> it= params.keySet().iterator();
        while (it.hasNext()){
            String key=it.next();
            String[] strArr=params.get(key);
            String value="";
            for(int i=0;i<strArr.length;i++){
                 value=(i==strArr.length-1)?value+strArr[i]:value+strArr[i]+",";
            }
            requestparams.put(key,value);
        }

         /**
          * 1.支付宝验签
          * 参数，公钥，编码，签名类型
          * */
        try {
            requestparams.remove("sign_type");
            boolean result= AlipaySignature.rsaCheckV2(requestparams,Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if (!result){
                return ServerResponse.serverResponseByError("非法请求,验证不通过");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return orderService.alipay_callback(requestparams);
    }

    /**
     * 查看订单的支付状态
     * */
    @RequestMapping(value = "/query_order_pay_status/orderNo/{orderNo}")
    public ServerResponse query_order_pay_status(HttpSession session,
                                                 @PathVariable("orderNo") Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }

        return orderService.query_order_pay_status(orderNo);
    }


}
