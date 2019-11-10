package org.yuan.spring.framework.aop;

import java.lang.reflect.Method;

public class AfterThrowAdvice extends AbstractAspectAdvice implements MethodInterceptor {
    private String throwName;
    private MethodInvocation mi;

    public AfterThrowAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void setThrowName(String throwName) {
        this.throwName = throwName;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        }
        catch (Exception ex) {
            invokeAdviceMethod(mi, null, ex.getCause());
            throw ex;
        }
    }
}
