package org.yuan.spring.framework.aop;

public interface MethodInterceptor {

    Object invoke(MethodInvocation mi) throws Throwable;

}
