package org.yuan.spring.framework.core;

public interface BeanFactory {

    /**
     * 根据名称获取对象
     * @param name
     * @return
     * @throws Exception
     */
    Object getBean(String name) throws Exception;

    /**
     * 根据类型获取对象
     * @param type
     * @return
     * @throws Exception
     */
    Object getBean(Class<?> type) throws Exception;

}
