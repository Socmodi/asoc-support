package org.asocframework.suppot.test;

import org.asocframework.support.validator.Valid;
import org.asocframework.support.validator.Validator;

/**
 * @author jiqing
 * @version $Id: TestModel，v 1.0 2017/11/10 15:38 jiqing Exp $
 * @desc
 */
@Validator
public class TestModel {

    @Valid(minValue = "10")
    private int age;

    @Valid()
    private String name;

    public TestModel(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
