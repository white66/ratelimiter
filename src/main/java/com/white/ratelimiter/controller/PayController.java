package com.white.ratelimiter.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.white.ratelimiter.annotation.MyLimiter;
import com.white.ratelimiter.bean.BaseResult;
import com.white.ratelimiter.serviceImpl.MassageServiceImpl;
import com.white.ratelimiter.serviceImpl.PayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * 功能模块()
 *
 * @Author white Liu
 * @Date 2020/7/31 10:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class PayController {
    @Autowired
    PayServiceImpl payService;
    @Autowired
    MassageServiceImpl massageService;
    /**
     * RateLimiter.create(2)表示以固定速率2r/s，即以每秒2个令牌的速率放至到令牌桶中
     */
    private RateLimiter rateLimiter = RateLimiter.create(1);
    @GetMapping("/pay/get")
    public BaseResult pay(){
        //限流处理，客户端请求从桶中获取令牌，如果在500毫秒没有获取到令牌，则直接走服务降级处理
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if(!tryAcquire){
            log.info("请求过多，请稍后");
            return BaseResult.error(500,"当前请求过多！");
        }
        int ref = payService.doPay(BigDecimal.valueOf(99.8));
        if(ref>0){
            log.info("支付成功");
            return BaseResult.ok().put("msg","支付成功");
        }
        return BaseResult.error(400,"支付失败,请重新尝试");
    }

    @GetMapping("/msg/get")
    @MyLimiter(rate = 1.0,timeout = 500)
    public BaseResult sendMsg(){
        boolean flag = massageService.sendMsg("恭喜您成长值+1");
        if (flag){
            log.info( "消息发送成功");
            return BaseResult.ok("消息发送成功");
        }
        return BaseResult.error(400,"消息发送失败，请重新尝试一次吧...");
    }

}
