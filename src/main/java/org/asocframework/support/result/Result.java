package org.asocframework.support.result;

import java.io.Serializable;

/**
 * @author jiqing
 * @version $Id: Result，v 1.0 2017/11/10 16:04 jiqing Exp $
 * @desc
 */
public class Result <T extends Serializable> implements Serializable{

    private T data;

    private StateCode stateCode;

    private Boolean success;

    private String statusText;

    public Result() {

    }

    public Result(T data, StateCode stateCode) {
        this.data = data;
        this.stateCode = stateCode;
    }

    public Result(T data, StateCode stateCode, Boolean success) {
        this.data = data;
        this.stateCode = stateCode;
        this.success = success;
    }

    public Result(StateCode stateCode, Boolean success, String statusText) {
        this.stateCode = stateCode;
        this.success = success;
        this.statusText = statusText;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public StateCode getStateCode() {
        return stateCode;
    }

    public void setStateCode(StateCode stateCode) {
        this.stateCode = stateCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

}
