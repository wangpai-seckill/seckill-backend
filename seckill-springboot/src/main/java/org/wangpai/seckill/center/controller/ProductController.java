package org.wangpai.seckill.center.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wangpai.seckill.center.response.DataType;
import org.wangpai.seckill.center.response.RspMsg;
import org.wangpai.seckill.center.service.ProductService;
import org.wangpai.seckill.center.session.SessionUtil;

import static org.wangpai.seckill.center.response.RspState.ERROR;
import static org.wangpai.seckill.center.response.RspState.SUCCESS;

/**
 * @since 2022-2-26
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * @since 2022-2-27
     */
    @PostMapping("/all")
    @ResponseBody
    @CrossOrigin
    public RspMsg all(HttpServletRequest request, HttpServletResponse response) {
        var products = this.productService.getProducts();
        return new RspMsg()
                .setState(SUCCESS)
                .setMsg("商品列表")
                .setDataType(DataType.JSON_OBJECT)
                .setData(products);
    }

    /**
     * @since 2022-2-27
     */
    @PostMapping("/{productId}")
    @ResponseBody
    @CrossOrigin
    public RspMsg getProduct(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable String productId) {
        System.out.println("getProduct called");
        System.out.println("session id：" + SessionUtil.getSessionId(request));

        var product = this.productService.getProduct(productId);
        if (product == null) {
            return new RspMsg().setState(ERROR).setMsg("该商品不存在");
        }
        return new RspMsg()
                .setState(SUCCESS)
                .setMsg("商品列表")
                .setDataType(DataType.JSON_OBJECT)
                .setData(product);
    }
}
