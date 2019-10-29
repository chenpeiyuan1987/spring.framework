package org.yuan.spring.framework.webmvc;

import org.yuan.spring.framework.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandlerAdapter {

    public boolean supports(Object handler) {
        return (handler instanceof HandlerMapping);
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        HandlerMapping handlerMapping = (HandlerMapping) handler;
        Map<String, Integer> paramMapping = new HashMap<>();

        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof RequestMapping) {
                    String paramName = ((RequestMapping) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramMapping.put(paramName, i);
                    }
                }
            }
        }

        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if (type == HttpServletResponse.class || type == HttpServletResponse.class) {
                paramMapping.put(type.getName(), i);
            }
        }

        Map<String, String[]> reqParameterMap = req.getParameterMap();

        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String, String[]> param : reqParameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            if (!paramMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = paramMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value, paramTypes[index]);
        }

        if (paramMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }

        if (paramMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = res;
        }

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        if (result == null) {
            return null;
        }

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
        if (isModelAndView) {
            return (ModelAndView) result;
        }
        return null;
    }

    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        }
        if (clazz == Integer.class) {
            return Integer.valueOf(value);
        }
        if (clazz == int.class) {
            return Integer.parseInt(value);
        }
        return null;
    }
}
