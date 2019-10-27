package org.yuan.spring.framework.webmvc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ModelAndView {
    /**
     * 模板名称
     */
    private String viewName;
    /**
     * 模板参数
     */
    private Map<String,?> model;

    public ModelAndView(String viewName) {
        this(viewName, null);
    }

}
