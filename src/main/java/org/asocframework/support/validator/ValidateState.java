package org.asocframework.support.validator;

public class ValidateState {
	
	private boolean pass = true;
	
	private String errorMsg = "";
	
	public ValidateState(){
		super();
	}
		
	public ValidateState(boolean pass, String errorMsg) {
		super();
		this.pass = pass;
		this.errorMsg = errorMsg;
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


}
