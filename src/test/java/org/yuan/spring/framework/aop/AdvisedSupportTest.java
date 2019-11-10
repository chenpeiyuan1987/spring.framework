package org.yuan.spring.framework.aop;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.yuan.spring.framework.demo.UserServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;

public class AdvisedSupportTest {

    @Test
    public void test() {
        AopConfig config = new AopConfig();
        config.setAspectBefore("before");
        config.setAspectAfter("after");
        config.setAspectAfterThrow("afterThrow");
        config.setPointCut("public .* org\\.yuan\\.spring\\.framework\\.demo\\..*ServiceImpl\\..*\\(.*\\)");
        config.setAspectAfterThrowName("java.lang.Exception");
        config.setAspectClass("org.yuan.spring.framework.demo.LogAspect");
        AdvisedSupport support = new AdvisedSupport(config);
        support.setTarget(new UserServiceImpl());
        support.setTargetClass(UserServiceImpl.class);

        assertThat(support.pointCutMatch(), Is.is(true));
    }

    @Test
    public void testIsAssignableFrom() {
        Class clazz = RuntimeException.class;
        Assert.assertTrue(Exception.class.isAssignableFrom(clazz));
    }

}
