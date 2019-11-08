package org.yuan.spring.framework.context;

import org.yuan.spring.framework.annotation.Autowired;
import org.yuan.spring.framework.annotation.Controller;
import org.yuan.spring.framework.annotation.Service;
import org.yuan.spring.framework.aop.AdvisedSupport;
import org.yuan.spring.framework.aop.AopConfig;
import org.yuan.spring.framework.aop.AopProxy;
import org.yuan.spring.framework.aop.JdkDynamicAopProxy;
import org.yuan.spring.framework.beans.*;
import org.yuan.spring.framework.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocations;
    private BeanDefinitionReader reader;

    private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<>();
    private Map<String,BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        // 定位配置文件
        reader = new BeanDefinitionReader(this.configLocations);

        // 加载配置文件
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        // 注册配置信息
        doRegisterBeanDefinition(beanDefinitions);

        // 类提前初始化
        doAutowired();
    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        for(BeanDefinition beanDefinition : beanDefinitions) {
            if(this.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception(String.format("The '%s' is exists.", beanDefinition.getFactoryBeanName()));
            }
            this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    private void doAutowired() {
        for(Map.Entry<String,BeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        try {
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if(instance == null) {
                return null;
            }

            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            BeanWrapper beanWrapper = new BeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName, beanWrapper);

            beanPostProcessor.postProcessAfterInitialization(instance, beanName);

            populateBean(beanName, instance);

            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        try {
            String className = beanDefinition.getBeanClassName();
            if(this.factoryBeanObjectCache.containsKey(className)) {
                return this.factoryBeanObjectCache.get(className);
            }

            Class<?> clazz = Class.forName(className);
            Object instance = clazz.newInstance();

            AdvisedSupport config = instantionAopConfig(beanDefinition);
            config.setTarget(instance);
            config.setTargetClass(clazz);

            if (config.pointCutMatch()) {
                instance = createProxy(config).getProxy();
            }

            this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(), instance);
            return instance;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private AopProxy createProxy(AdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        if (targetClass.getInterfaces().length > 0) {
            return new JdkDynamicAopProxy(config);
        }
        throw new RuntimeException("Target class must implement interface.");
    }

    private AdvisedSupport instantionAopConfig(BeanDefinition beanDefinition) {
        AopConfig config = new AopConfig();
        config.setPointCut(reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(reader.getConfig().getProperty("aspectClass"));
        config.setAspectAfter(reader.getConfig().getProperty("aspectAfter"));
        config.setAspectBefore(reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new AdvisedSupport(config);
    }

    private void populateBean(String beanName, Object instance) {
        Class<?> clazz = instance.getClass();
        if(!(clazz.isAnnotationPresent(Controller.class)
            || clazz.isAnnotationPresent(Service.class))) {
           return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if(!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            Autowired autowired = field.getAnnotation(Autowired.class);
            String autowiredBeanName = autowired.value().trim();
            if("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                //field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
                field.set(instance, this.getBean(autowiredBeanName));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Object getBean(Class<?> type) throws Exception {
        return getBean(type.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet()
            .toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
