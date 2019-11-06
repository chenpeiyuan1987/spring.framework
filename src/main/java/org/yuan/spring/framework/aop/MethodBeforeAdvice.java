package org.yuan.spring.framework.aop;

import java.lang.reflect.Method;

public class MethodBeforeAdvice extends AbstractAspectAdvice implements MethodInterceptor {
    private JoinPoint joinPoint;

    public MethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        invokeAdviceMethod(joinPoint, null, null);
        return mi.proceed();
    }
}
