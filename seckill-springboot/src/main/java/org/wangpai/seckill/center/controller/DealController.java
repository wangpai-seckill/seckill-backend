package org.wangpai.seckill.center.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.wangpai.seckill.center.service.DealService;
import org.wangpai.seckill.center.util.ServiceUtil;
import org.wangpai.seckill.persistence.complex.daodomain.SearchRequest;

import static org.wangpai.seckill.center.response.RspState.SUCCESS;

/**
 * @since 2022-2-27
 */
@Controller
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    /**
     * @param num 获取的数据条数
     * @since 2022-2-27
     */
    @PostMapping("/dealList/{num}")
    @ResponseBody
    public RspMsg getProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable String num)
            throws JsonProcessingException {
        var user = ServiceUtil.getUserFromCache(request, response).getUser();
        var searchRequest = new SearchRequest()
                .setUserPhone(user.getPhone())
                .setNum(Integer.parseInt(num));
        var deals = this.dealService.getDeals(searchRequest);
        return new RspMsg()
                .setState(SUCCESS)
                .setMsg("获取订单列表成功")
                .setDataType(DataType.JSON_OBJECT)
                .setData(deals);
    }

    /**
     * @param num 获取的数据条数
     * @since 2022-2-27
     * @deprecated 2022-3-4 此方法用于跨域测试
     */
    @PostMapping("/withoutCookies/dealList/{userPhone}/{num}")
    @ResponseBody
    @CrossOrigin
    @Deprecated
    public RspMsg getProductWithoutCookies(HttpServletRequest request, HttpServletResponse response,
                                           @PathVariable String userPhone, @PathVariable String num) {
        var searchRequest = new SearchRequest()
                .setUserPhone(userPhone)
                .setNum(Integer.parseInt(num));
        var deals = this.dealService.getDeals(searchRequest);
        return new RspMsg()
                .setState(SUCCESS)
                .setMsg("获取订单列表成功")
                .setDataType(DataType.JSON_OBJECT)
                .setData(deals);
    }
}
