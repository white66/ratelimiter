package com.white.ratelimiter.service;

import java.math.BigDecimal;

/**
 * 功能模块(模拟支付)
 *
 * @Author white Liu
 * @Date 2020/7/31 9:58
 * @Version 1.0
 */
public interface PayService {
    int doPay(BigDecimal price);
}
