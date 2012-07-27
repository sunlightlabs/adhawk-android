package com.sunlightfoundation.adhawk.android;

public class AdHawkException extends RuntimeException{
	private static final long serialVersionUID = 5L;

	public AdHawkException (String msg) {
		super(msg);
	}
	
	public AdHawkException (String msg, Exception e) {
		super(msg,e);
	}

}
