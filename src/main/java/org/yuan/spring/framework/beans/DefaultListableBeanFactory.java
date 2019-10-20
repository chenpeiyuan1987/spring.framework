package org.yuan.spring.framework.beans;

import org.yuan.spring.framework.context.AbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractApplicationContext {
    /** 存储注册的BeanDefinition */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
}
