package com.controller.portal;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.UserInfo;
import com.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    ICartService cartService;

    /**
     * 购物车中添加商品
     * */
    @RequestMapping("/add/{productId}/{count}")
    public ServerResponse add(HttpSession session,
                              @PathVariable("productId") Integer productId,
                              @PathVariable("count") Integer count){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
       if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }

        return cartService.add(userInfo.getId(),productId,count);
    }

    /**
     * 购物车列表
     * */
    @RequestMapping("/list.do")
    public ServerResponse list(HttpSession session){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.list(userInfo.getId());
    }

    /**
     * 更新购物车某个商品数量
     * */
    @RequestMapping("/update/{productId}/{count}")
    public ServerResponse update(HttpSession session,
                                 @PathVariable("productId") Integer productId,
                                 @PathVariable("count") Integer count){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.update(userInfo.getId(),productId,count);
    }

    /**
     * 移除购物车某个商品
     * */
    @RequestMapping("/delete_product/productIds/{productId}")
    public ServerResponse delete_product(HttpSession session,
                                         @PathVariable("productId") String productIds){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.delete_product(userInfo.getId(),productIds);
    }

    /**
     * 购物车选中某个商品
     * */
    @RequestMapping("/select/productId/{productId}")
    public ServerResponse select(HttpSession session,
                                 @PathVariable("productId") Integer productId){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     * 购物车取消选中某个商品
     * */
    @RequestMapping("/un_select/productId/{productId}")
    public ServerResponse un_select(HttpSession session,
                                    @PathVariable("productId") Integer productId){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     * 购物车全选
     * */
    @RequestMapping("/select_all.do")
    public ServerResponse select_all(HttpSession session){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     * 购物车取消全选
     * */
    @RequestMapping("/un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     * 查询购物车中商品数量
     * */
    @RequestMapping("/get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){
        //用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("需要登录");
        }
        return cartService.get_cart_product_count(userInfo.getId());
    }

}
