package org.asocframework.suppot.test;

import org.asocframework.support.validator.Valid;
import org.asocframework.support.validator.ValidateState;
import org.asocframework.support.validator.ValidateTools;
import org.asocframework.support.validator.Validator;
import org.junit.Assert;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author jiqing
 * @version $Id: ModelService，v 1.0 2017/12/4 17:36 jiqing Exp $
 * @desc
 */
public class ModelService {

    @Validator(alias = "service")
    public ValidateState service(@Valid(name = "name",defaultValue = "model")String name, @Valid(name = "id",minValue = "10") Integer id){
        ValidateState validateState = ValidateTools.volidate(this.getClass().getName()+".service",name,id);
        return validateState;
    }


    @Validator(alias = "service2")
    public ValidateState service2(@Valid(name = "name",defaultValue = "model")String name,@Valid(name = "id",minValue = "10") Integer id,
                                  @Valid(name = "list",minValue = "10") List<Integer> list){
        ValidateState validateState = ValidateTools.volidate(this.getClass().getName()+".service2",name,id,list);
        name = (String) validateState.getPram("name");
        System.out.println("name:"+name +",id:"+id);
        return validateState;
    }


    @Validator(alias = "service3")
    public ValidateState service3(@Valid(defaultValue = "model")String name,@Valid(minValue = "10") Integer id,
                                  @Valid List<SubModel> list){
        ValidateState validateState = ValidateTools.volidate(this.getClass().getName()+".service3",name,id,list);

        return validateState;
    }

    public void change(Integer i) throws Exception{
        Constructor<Unsafe> constructor = Unsafe.class.getDeclaredConstructor(new Class<?>[0]);
        constructor.setAccessible(true);
        Unsafe unsafe = constructor.newInstance(new Object[0]);
        Assert.assertNotNull(unsafe);
        long offset = unsafe.getAddress(i);
        unsafe.putInt(offset,3);
        System.out.println(i);
    }

}
