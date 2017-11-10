package org.asocframework.suppot.test;

import org.asocframework.support.validator.ValidateState;
import org.asocframework.support.validator.ValidateTools;
import org.junit.Test;

/**
 * @author jiqing
 * @version $Id: ModelTest，v 1.0 2017/11/10 15:38 jiqing Exp $
 * @desc
 */

public class ModelTest {

    @Test
    public void volidateTest(){
        TestModel model = new TestModel(1,"model");
        ValidateState state = ValidateTools.volidate(model);
        System.out.println(state.isPass());
    }

}
