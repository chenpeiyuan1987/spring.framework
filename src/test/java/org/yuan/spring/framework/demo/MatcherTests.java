package org.yuan.spring.framework.demo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatcherTests {

    @Test
    public void test() {
        System.out.println("123456");
    }

    @Test
    public void testReplaceFirst() {
        String line = "Hello ${name}. ${other} I'm ${sex}!";
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
