package com.controller.portal;

import com.common.ServerResponse;
import com.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    IProductService productService;

    /**
     * 商品详情
     * */
    @RequestMapping(value = "/detail/productId/{productId}")
    public ServerResponse detail_portal(@PathVariable("productId") Integer productId){
        return productService.detail_portal(productId);
    }

    /**
     * 搜索商品并排序
     * */
    @RequestMapping(value = "/list/categoryId/{categoryId}/{pageNum}/{pageSize}/{orderBy}")
    public ServerResponse listRestfulByCategoryId(
                                      @PathVariable("categoryId") Integer categoryId,
                                      @PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize,
                                      @PathVariable("orderBy") String orderBy){

        return productService.list_portal(categoryId,null,pageNum,pageSize,orderBy);
    }

    @RequestMapping(value = "/list/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}")
    public ServerResponse listRestfulByKeyword(
                                      @PathVariable("keyword") String keyword,
                                      @PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize,
                                      @PathVariable("orderBy") String orderBy){

        return productService.list_portal(null,keyword,pageNum,pageSize,orderBy);
    }

}
