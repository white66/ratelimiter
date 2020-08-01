package com.white.ratelimiter.serviceImpl;

import com.white.ratelimiter.service.MassageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 功能模块(发送消息服务)
 *
 * @Author white Liu
 * @Date 2020/7/31 10:06
 * @Version 1.0
 */
@Service
@Slf4j
public class MassageServiceImpl implements MassageService {
    @Override
    public boolean sendMsg(String msg) {
        log.info("消息发送成功！");
        return true;
    }
}
