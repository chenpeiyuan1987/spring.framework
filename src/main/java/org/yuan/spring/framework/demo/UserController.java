package org.yuan.spring.framework.demo;

import org.yuan.spring.framework.annotation.Autowired;
import org.yuan.spring.framework.annotation.Controller;
import org.yuan.spring.framework.annotation.RequestMapping;
import org.yuan.spring.framework.annotation.RequestParam;
import org.yuan.spring.framework.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest req, HttpServletResponse res) {
        List<Person> list = userService.list();
        try {
            res.getWriter().write(list.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/page")
    public ModelAndView page(@RequestParam("name") String name) {
        Map<String, String> model = new HashMap<>();
        model.put("name", name);
        model.put("date", new Date().toString());
        return new ModelAndView("page.htm", model);
    }
}
