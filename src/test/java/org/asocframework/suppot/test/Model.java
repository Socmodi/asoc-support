package org.asocframework.suppot.test;


import org.asocframework.support.validator.Valid;
import org.asocframework.support.validator.Validator;

import java.util.List;

/**
 * @author jiqing
 * @version $Id: Model，v 1.0 2017/12/4 17:36 jiqing Exp $
 * @desc
 */
@Validator
public class Model {


    @Valid(minValue = "10",defaultValue = "12")
    private int age;

    @Valid(defaultValue = "model")
    private String name;

    @Valid(minValue = "10")
    private List<Integer> list;

    @Valid
    private List<SubModel> subs;


    public Model(String name) {
        this.name = name;
    }

    public Model(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public Model(int age, String name, List<Integer> list) {
        this.age = age;
        this.name = name;
        this.list = list;
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

    public List<SubModel> getSubs() {
        return subs;
    }

    public void setSubs(List<SubModel> subs) {
        this.subs = subs;
    }
}
