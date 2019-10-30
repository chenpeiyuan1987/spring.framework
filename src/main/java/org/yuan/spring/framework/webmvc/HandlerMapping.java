package org.yuan.spring.framework.webmvc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class HandlerMapping {
    /**
     * 控制器
     */
    private Object controller;
    /**
     * 匹配器
     */
    private Pattern pattern;
    /**
     * 执行方法
     */
    private Method method;

}
