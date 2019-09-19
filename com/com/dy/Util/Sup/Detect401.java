package com.dy.Util.Sup;

public class Detect401 {
	private int code;
	private String message;

	public Detect401() {
		this.code = 200;
		this.message = "ok";
	}

	public Detect401(int st) {
		this.code = st;
		this.message = "unknown";
	}
	
	public Detect401(int st,String msg) {
		this.code = st;
		this.message = msg;
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