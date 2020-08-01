package com.white.ratelimiter.annotation;

/**
 * 功能模块(令牌桶限流注解)
 *
 * @Author white Liu
 * @Date 2020/8/1 11:55
 * @Version 1.0
 */

import java.lang.annotation.*;

@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLimiter {
    //向令牌桶放入令牌的速率
    double rate();
    //从令牌桶获取令牌的超时时间
    long timeout() default 0;
}
