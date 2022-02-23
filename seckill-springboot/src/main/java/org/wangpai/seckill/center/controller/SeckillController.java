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
import org.wangpai.seckill.center.access.AccessLimit;
import org.wangpai.seckill.center.response.RspMsg;
import org.wangpai.seckill.center.service.SeckillService;
import org.wangpai.seckill.center.service.UserService;
import org.wangpai.seckill.center.util.ServiceUtil;
import org.wangpai.seckill.middleware.cache.util.CacheUtil;

import static org.wangpai.seckill.center.response.RspState.ERROR;
import static org.wangpai.seckill.center.response.RspState.OTHER;
import static org.wangpai.seckill.center.response.RspState.SUCCESS;
import static org.wangpai.seckill.middleware.cache.type.CacheType.SECKILL_REQUEST;

/**
 * @since 2022-2-27
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final SeckillService seckillService;

    private final UserService userService;

    public SeckillController(SeckillService seckillService, UserService userService) {
        this.seckillService = seckillService;
        this.userService = userService;
    }

    /**
     * @since 2022-2-28
     */
    @PostMapping("/{productId}")
    @ResponseBody
    @AccessLimit(needLogin = true)
    public RspMsg seckill(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable String productId)
            throws JsonProcessingException {
        var user = ServiceUtil.getUserFromCache(request, response).getUser();

        var seckillResult = this.seckillService.makeSeckillRequest(productId, user.getPhone());
        return switch (seckillResult.getResult()) {
            case SUCCESS -> new RspMsg()
                    .setState(SUCCESS)
                    .setData(seckillResult.getRequestId())
                    .setMsg("请求已提交，等待处理");
            case FAIL -> new RspMsg().setState(ERROR).setMsg(seckillResult.getReason());

            // 此分支不应该发生
            default -> new RspMsg().setState(ERROR).setMsg("未知错误");
        };
    }

    /**
     * @since 2022-2-28
     */
    @PostMapping("/result/{requestId}")
    @ResponseBody
    @AccessLimit(needLogin = true)
    public RspMsg getSeckillResult(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable String requestId)
            throws JsonProcessingException {
        var seckillResult = this.seckillService.getSeckillResult(request, response, requestId);
        return switch (seckillResult.getResult()) {
            case SUCCESS -> {
                // 如果该请求已被处理完，在缓存中消费（删除）该请求
                CacheUtil.del(requestId, SECKILL_REQUEST);
                yield new RspMsg().setState(SUCCESS).setMsg("秒杀成功");
            }

            case FAIL -> {
                // 如果该请求已被处理完，在缓存中消费（删除）该请求
                CacheUtil.del(requestId, SECKILL_REQUEST);
                yield new RspMsg().setState(ERROR).setMsg(seckillResult.getReason());
            }

            case WAIT -> new RspMsg().setState(OTHER).setMsg("请求已提交，等待处理");

            // 此分支不应该发生
            default -> new RspMsg().setState(ERROR).setMsg("未知错误");
        };
    }

    /**
     * @since 2022-2-28
     * @deprecated 2022-3-4 此方法用于跨域测试
     */
    @PostMapping("/withoutCookies/{productId}/{userPhone}")
    @ResponseBody
    @CrossOrigin
    @Deprecated
    public RspMsg seckillWithoutCookies(HttpServletRequest request, HttpServletResponse response,
                                        @PathVariable String productId, @PathVariable String userPhone)
            throws JsonProcessingException {
        var seckillResult = this.seckillService.makeSeckillRequest(productId, userPhone);
        return switch (seckillResult.getResult()) {
            case SUCCESS -> new RspMsg()
                    .setState(SUCCESS)
                    .setData(seckillResult.getRequestId())
                    .setMsg("请求已提交，等待处理");
            case FAIL -> new RspMsg().setState(ERROR).setMsg(seckillResult.getReason());

            // 此分支不应该发生
            default -> new RspMsg().setState(ERROR).setMsg("未知错误");
        };
    }

    /**
     * @since 2022-2-28
     * @deprecated 2022-3-4 此方法用于跨域测试
     */
    @PostMapping("/withoutCookies/result/{userPhone}/{requestId}")
    @ResponseBody
    @CrossOrigin
    @Deprecated
    public RspMsg getSeckillResultWithoutCookies(HttpServletRequest request, HttpServletResponse response,
                                                 @PathVariable String userPhone, @PathVariable String requestId)
            throws JsonProcessingException {
        var seckillResult = this.seckillService.getSeckillResultWithoutCookies(
                request, response, userPhone, requestId);
        return switch (seckillResult.getResult()) {
            case SUCCESS -> {
                // 如果该请求已被处理完，在缓存中消费（删除）该请求
                CacheUtil.del(requestId, SECKILL_REQUEST);
                yield new RspMsg().setState(SUCCESS).setMsg("秒杀成功");
            }

            case FAIL -> {
                // 如果该请求已被处理完，在缓存中消费（删除）该请求
                CacheUtil.del(requestId, SECKILL_REQUEST);
                yield new RspMsg().setState(ERROR).setMsg(seckillResult.getReason());
            }

            case WAIT -> new RspMsg().setState(OTHER).setMsg("请求已提交，等待处理");

            // 此分支不应该发生
            default -> new RspMsg().setState(ERROR).setMsg("未知错误");
        };
    }
}
