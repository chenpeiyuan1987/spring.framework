package org.yuan.spring.framework.demo;

import lombok.extern.slf4j.Slf4j;
import org.yuan.spring.framework.annotation.Service;

import javax.swing.text.html.HTMLDocument;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    public List<Person> list() {
        List<Person> list = new ArrayList<>();
        Person person = new Person();
        person.setName("Yuan");
        person.setAge(33);
        person.setSex("male");
        list.add(person);
        return list;
    }

    @Override
    public Object add(Person person) {
        throw new RuntimeException("添加失败");
    }

    @Override
    public Object alt(Person person) {
        try {
            Thread.sleep(1000);
            return true;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object del(Serializable id) {
        return null;
    }
}
