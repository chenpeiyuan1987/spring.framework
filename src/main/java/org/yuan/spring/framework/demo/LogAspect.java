package org.yuan.spring.framework.demo;

import lombok.extern.slf4j.Slf4j;
import org.yuan.spring.framework.aop.JoinPoint;

import java.util.concurrent.Executors;

@Slf4j
public class LogAspect {

    public void before(JoinPoint joinPoint) {
        joinPoint.setUserAttribute("start", System.currentTimeMillis());
        log.info("Method Before - {}, {}, {}", joinPoint.getThis(), joinPoint.getMethod(), joinPoint.getArguments());
    }

    public void after(JoinPoint joinPoint) {
        long start = (long)joinPoint.getUserAttribute("start");
        long ms = System.currentTimeMillis() - start;
        log.info("Method After - {}, {}, {}, {}", ms, joinPoint.getThis(), joinPoint.getMethod(), joinPoint.getArguments());
    }

    public  void afterThrow(JoinPoint joinPoint, Throwable ex) {
        log.info("After Throw - {}, {}, {}, {}", ex.getMessage(), joinPoint.getThis(), joinPoint.getMethod(), joinPoint.getArguments());
    }
}
