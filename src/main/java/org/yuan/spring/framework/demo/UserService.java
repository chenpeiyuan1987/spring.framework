package org.yuan.spring.framework.demo;

import java.io.Serializable;
import java.util.List;

public interface UserService {

    List<Person> list();

    Object add(Person person);

    Object alt(Person person);

    Object del(Serializable id);
}
