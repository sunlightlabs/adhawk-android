package org.cuiBono;

public class CuiBonoException extends RuntimeException{
	private static final long serialVersionUID = 5L;

	public CuiBonoException (String msg) {
		super(msg);
	}
	
	public CuiBonoException (String msg, Exception e) {
		super(msg,e);
	}

}
