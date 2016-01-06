package com.ericsson.a2;

public class AppError 
{
	int code;
	String message;
	
	public AppError() 
	{
		code = 0;
		message = "Ok";
	}

	public AppError(Throwable e)  
	{
		code = 1;
		message = "Exception caught: " + e.getMessage();
	}

	public AppError(int code, String message) 
	{
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
