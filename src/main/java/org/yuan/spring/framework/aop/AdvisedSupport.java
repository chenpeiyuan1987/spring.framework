package org.yuan.spring.framework.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvisedSupport {
    private Class targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;
    private AopConfig config;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);

        if (cached == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            methodCache.put(m, cached);
        }
        return cached;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(targetClass.toString()).matches();
    }

    private void parse() {
        String pointCut = config.getPointCut();
        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));
        methodCache = new HashMap<>();
        Pattern pattern = Pattern.compile(pointCut);

        try {
            Class aspectClass = Class.forName(config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method m : aspectClass.getMethods()) {
                aspectMethods.put(m.getName(), m);
            }

            for (Method m : targetClass.getMethods()) {
                String methodString = m.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()) {
                    List<Object> advices = new LinkedList<>();
                    if (isNotBlank(config.getAspectBefore())) {
                        advices.add(new MethodBeforeAdvice(aspectMethods.get(config.getAspectBefore()), aspectClass.newInstance()));
                    }
                    if (isNotBlank(config.getAspectAfter())) {
                        advices.add(new AfterReturnAdvice(aspectMethods.get(config.getAspectAfter()), aspectClass.newInstance()));
                    }
                    if (isNotBlank(config.getAspectAfterThrow())) {
                        AfterThrowAdvice afterThrowAdvice = new AfterThrowAdvice(aspectMethods.get(config.getAspectAfterThrow()), aspectClass.newInstance());
                        afterThrowAdvice.setThrowName(config.getAspectAfterThrowingName());
                        advices.add(afterThrowAdvice);
                    }
                    methodCache.put(m, advices);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isNotBlank(String str) {
        if (str == null) {
            return false;
        }
        if ("".equals(str.trim())) {
            return false;
        }
        return true;
    }
}
