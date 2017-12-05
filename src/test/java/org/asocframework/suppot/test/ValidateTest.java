package org.asocframework.suppot.test;

import org.asocframework.support.validator.ValidateState;
import org.asocframework.support.validator.ValidateTools;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author jiqing
 * @version $Id: ValidateTest，v 1.0 2017/12/4 17:35 jiqing Exp $
 * @desc
 */
public class ValidateTest {

    ModelService service = new ModelService();

    @Test
    public void volidateTest(){
        Model model = new Model("test");
        ValidateState state = ValidateTools.volidate(model);
        System.out.println(state.isPass());
    }

    @Test
    public void volidateTest2(){
        ValidateTools.resolve(service.getClass());
        ValidateState state = service.service("test",1);
        System.out.println(state.isPass());
    }

    /**
     * 测试collection 内部为基础数据
     */
    @Test
    public void volidateTest3(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        Model model = new Model(11,"test",list);
        ValidateState state = ValidateTools.volidate(model);
        System.out.println(state.isPass());
    }

    /**
     * 测试collection 内部为复合对象
     */
    @Test
    public void volidateTest4(){
        ArrayList<Integer> list = new ArrayList();
        ArrayList<SubModel> subModels = new ArrayList();
        subModels.add(new SubModel(9,"sdfsdf"));
        list.add(11);
        Model model = new Model(11,"test",list);
        model.setSubs(subModels);
        ValidateState state = ValidateTools.volidate(model);
        System.out.println(state.isPass());
    }


    /**
     * 方法层级,局部变量;测试collection 内部为复合对象
     */
    @Test
    public void volidateTest5(){
        ArrayList<SubModel> subModels = new ArrayList();
        subModels.add(new SubModel(9,"sdfsdf"));
        ValidateTools.resolve(service.getClass());
        ValidateState state = service.service3("test",11,subModels);
        System.out.println(state.isPass());
    }


    /**
     * 方法层级,局部变量;测试collection 内部为基础数据
     */
    @Test
    public void volidateTest6(){
        ArrayList<Integer> list = new ArrayList();
        list.add(6);
        ValidateTools.resolve(service.getClass());
        ValidateState state = service.service2("test",11,list);
        System.out.println(state.isPass());
    }

    @Test
    public void unsafeTest() throws Exception {
        Integer i = new Integer(11);
        service.change(i);
        System.out.println(i);
    }

}
