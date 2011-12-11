package org.cuiBono;

public class CuiBonoException extends RuntimeException{
	
	public CuiBonoException (String msg) {
		super(msg);
	}
	
	public CuiBonoException (String msg, Exception e) {
		super(msg,e);
	}

}
