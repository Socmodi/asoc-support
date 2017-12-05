package org.asocframework.support.result;

/**
 * @author jiqing
 * @version $Id: CommonStateCode，v 1.0 2017/11/10 16:28 jiqing Exp $
 * @desc
 */
public class CommonStateCode {

    public static final StateCode SUCCESS = new StateCode(10000,"成功");

    public static final StateCode FAIL = new StateCode(-10000,"失败");

    public static final  StateCode PARAM_ERROR = new StateCode(-10001,"参数错误");

}
