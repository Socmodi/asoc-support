package org.asocframework.support.result;

import java.io.Serializable;

/**
 * @author jiqing
 * @version $Id: Results，v 1.0 2017/11/10 16:04 jiqing Exp $
 * @desc
 */
public class Results {

    private Results() {
    }

    public static <T extends Serializable> Result  newSuccessResult(T data){
        return new Result<>(data,CommonStateCode.SUCCESS,true);
    }

    public static Result  newFailResult(StateCode stateCode,String statusTxt){
        return new Result(stateCode,false,statusTxt);
    }

}
