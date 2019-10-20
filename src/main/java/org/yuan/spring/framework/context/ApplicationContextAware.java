package org.yuan.spring.framework.context;

public interface ApplicationContextAware {

    /**
     * 设置上下文
     * @param applicationContext
     */
    void setApplicationContext(ApplicationContext applicationContext);
}
