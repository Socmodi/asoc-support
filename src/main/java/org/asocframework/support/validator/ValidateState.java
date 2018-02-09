package org.asocframework.support.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidateState {

	private boolean pass = true;

	private String errorMsg = "";

	private Object defaultValue ;

	private Object[] args;

	private Map<String,Object> params = new HashMap<>();

	public ValidateState(){
		super();
	}

	public ValidateState(boolean pass, String errorMsg) {
		super();
		this.pass = pass;
		this.errorMsg = errorMsg;
	}

	public void removerDefaultValue(){
		this.defaultValue = null;
	}

	public void putParam(String key ,Object value){
		params.put(key,value);
	}

	public Object getPram(String key){
		return params.get(key);
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}


}
