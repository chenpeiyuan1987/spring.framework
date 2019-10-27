package org.yuan.spring.framework.webmvc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class HandlerMapping {
    /**
     *
     */
    private Object controller;
    /**
     *
     */
    private Pattern pattern;
    /**
     *
     */
    private Method method;

}
