package org.yuan.spring.framework.demo;

import lombok.extern.slf4j.Slf4j;
import org.yuan.spring.framework.annotation.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    public List<Person> list() {
        List<Person> list = new ArrayList<>();
        Person person = new Person();
        person.setName("Yuan");
        person.setAge(33);
        person.setSex("male");
        list.add(person);
        return list;
    }

}
