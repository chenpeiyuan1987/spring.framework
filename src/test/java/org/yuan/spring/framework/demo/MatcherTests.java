package org.yuan.spring.framework.demo;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatcherTests {

    private String poingCut = "public .* org\\.yuan\\.spring\\.framework\\.demo\\..*ServiceImpl\\..*\\(.*\\)";

    @Test
    public void testMatchClass() {
        String pointCutForClass = "class " + poingCut.substring(poingCut.lastIndexOf(" ") + 1, poingCut.lastIndexOf("(") - 5);
        Class clazz = UserServiceImpl.class;
        System.out.println(pointCutForClass);
        System.out.println(clazz.toString());

        assertThat(clazz.toString().matches(pointCutForClass), is(true));
    }

    @Test
    public void testMatchMethod() {
        Pattern pattern = Pattern.compile(poingCut);
        Class clazz = UserServiceImpl.class;
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println(method.toString());
            Matcher matcher = pattern.matcher(method.toString());
            assertThat(matcher.matches(), is(true));
        }
    }

    @Test
    public void test() {
        System.out.println("123456");
    }

    @Test
    public void testReplaceFirst() {
        String line = "<h1>Hello ${name}. I'm ${age}.</h1>";
        Pattern pattern = Pattern.compile("\\$\\{[^}]+\\}");
        Matcher matcher = pattern.matcher(line);

        Map<String, String> model = new HashMap<>();
        model.put("name", "chen");
        model.put("sex", "male");

        int start = 0;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            sb.append(line.substring(start, matcher.start()));
            start = matcher.end();
            String group = matcher.group();
            String param = group.replaceAll("\\$\\{|\\}", "");
            param = model.get(param);
            if (param == null) {
                sb.append(group);
            }
            else {
                sb.append(param);
            }
        }
        sb.append(line.substring(start));
        assertThat(sb.toString(), is("Hello chen. ${other} I'm male!"));
    }
}
