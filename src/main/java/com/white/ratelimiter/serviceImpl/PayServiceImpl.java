package com.white.ratelimiter.serviceImpl;

import com.white.ratelimiter.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 功能模块(模拟支付)
 *
 * @Author white Liu
 * @Date 2020/7/31 10:03
 * @Version 1.0
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {
    @Override
    public int doPay(BigDecimal price) {
        log.info("已成功支付"+price);
        //给用户银行账户进行扣款操作，同时给目标用户账户进行加额操作，主要要开启事务，保证一致性
        return 1;
    }
}
