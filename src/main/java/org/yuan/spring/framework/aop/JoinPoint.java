package org.yuan.spring.framework.aop;

import java.lang.reflect.Method;

public interface JoinPoint {

    Method getMethod();

    Object[] getArguments();

    Object getThis();

    void setUserAttribute(String key, Object val);

    Object getUserAttribute(String key);

}
