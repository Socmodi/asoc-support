package org.asocframework.suppot.test;


import org.asocframework.support.validator.Valid;

/**
 * @author jiqing
 * @version $Id: SubModel，v 1.0 2017/12/5 14:12 jiqing Exp $
 * @desc
 */
public class SubModel {


    @Valid(minValue = "10")
    private int sage;

    @Valid(defaultValue = "model")
    private String sname;

    public SubModel(int sage, String sname) {
        this.sage = sage;
        this.sname = sname;
    }

}
