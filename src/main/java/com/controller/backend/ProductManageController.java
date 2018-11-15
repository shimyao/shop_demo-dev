package com.controller.backend;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.Product;
import com.pojo.UserInfo;
import com.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @Autowired
    IProductService productService;

    /**
     * 后台--新增或更新产品
     * */
    @RequestMapping(value = "/save.do")
    public ServerResponse saveOrUpdate(HttpSession session, Product product){

        return productService.saveOrUpdate(product);
    }

    /**
     * 后台--产品上下架
     * */
    @RequestMapping(value = "/set_sale_status/{productId}/{status}")
    public ServerResponse set_sale_status(HttpSession session,
                                          @PathVariable("productId") Integer productId,
                                          @PathVariable("status") Integer status){

        return productService.set_sale_status(productId,status);
    }

    /**
     * 后台--查看商品详情
     * */
    @RequestMapping(value = "/detail/productId/{productId}")
    public ServerResponse detail(HttpSession session,
                                 @PathVariable("productId") Integer productId){

        return productService.detail(productId);
    }

    /**
     * 后台--查看商品列表
     * */
    @RequestMapping(value = "/list/{pageNum}/{pageSize}")
    public ServerResponse list(HttpSession session,
                               @PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize){

        return productService.list(pageNum,pageSize);
    }

    /**
     * 后台--产品搜索接口
     * */
    @RequestMapping(value = "/search/productId/{productId}/{pageNum}/{pageSize}")
    public ServerResponse searchRestfulByProductId(HttpSession session,
                               @PathVariable("productId")  Integer productId,
                               @PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize")Integer pageSize){

        return productService.search(productId,null,pageNum,pageSize);
    }
    @RequestMapping(value = "/search/productName/{productName}/{pageNum}/{pageSize}")
    public ServerResponse searchRestfulByProductName(HttpSession session,
                                 @PathVariable("productName") String productName,
                                 @PathVariable("pageNum") Integer pageNum,
                                 @PathVariable("pageSize")Integer pageSize){

        return productService.search(null,productName,pageNum,pageSize);
    }

}
