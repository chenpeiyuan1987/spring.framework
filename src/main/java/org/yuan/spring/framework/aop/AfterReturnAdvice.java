package org.yuan.spring.framework.aop;

import java.lang.reflect.Method;

public class AfterReturnAdvice extends AbstractAspectAdvice implements MethodInterceptor {
    private JoinPoint joinPoint;

    public AfterReturnAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        invokeAdviceMethod(joinPoint, retVal, null);
        return retVal;
    }
}
