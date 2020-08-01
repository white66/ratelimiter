package com.white.ratelimiter.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.white.ratelimiter.annotation.MyLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * 功能模块()
 *
 * @Author white Liu
 * @Date 2020/8/1 11:59
 * @Version 1.0
 */
@Component
@Aspect
@Slf4j
@Scope
public class MyRateLimiterAspect {
    @Autowired
    private HttpServletResponse response;//注入HttpServletResponse对象，进行降级处理，返回必要信息提示用户

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Pointcut("execution(public * com.white.ratelimiter.controller.*.*(..))")
    public void cutPoint(){}
    @Around("cutPoint()")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();

        //使用反射获取方法上是否存在@MyRateLimiter注解
        MyLimiter myRateLimiter = signature.getMethod().getDeclaredAnnotation(MyLimiter.class);
        if(myRateLimiter == null){
            //程序正常执行，执行目标方法
            return proceedingJoinPoint.proceed();
        }
        //获取注解上的参数
        //获取配置的速率
        double rate = myRateLimiter.rate();
        //获取客户端等待令牌的时间
        long timeout = myRateLimiter.timeout();

        //设置限流速率
        rateLimiter.setRate(rate);

        //判断客户端获取令牌是否超时
        boolean tryAcquire = rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        if(!tryAcquire){
            log.info("当前访问太多，请稍后尝试");
            //服务降级
            callback();
            return null;
        }
        //获取到令牌，直接执行
        return proceedingJoinPoint.proceed();
    }
    /**
     * 降级处理
     */
    private void callback() {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer =  response.getWriter();
            writer.println("出错了，请重新尝试");
            writer.flush();;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
