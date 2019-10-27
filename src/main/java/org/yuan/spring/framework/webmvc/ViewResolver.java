package org.yuan.spring.framework.webmvc;

import lombok.Getter;

import java.io.File;
import java.util.Locale;

public class ViewResolver {
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    @Getter
    private String viewName;

    public ViewResolver(String templateRoot) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String templateRootPath = classLoader.getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception {
        this.viewName = viewName;
        if (viewName == null || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName
            : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName)
            .replaceAll("/+", "/"));
        return new View(templateFile);
    }

}
