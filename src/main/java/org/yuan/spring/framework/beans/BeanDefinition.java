package org.yuan.spring.framework.beans;

import lombok.Data;

@Data
public class BeanDefinition {
    private String beanClassName;
    private String factoryBeanName;
    private boolean lazyInit = false;

}
