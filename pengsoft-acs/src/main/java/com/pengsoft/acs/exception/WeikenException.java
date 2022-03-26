package com.pengsoft.acs.exception;

/**
 * Weiken Exception
 * 
 * @author peng.dang@pengsoft.com
 * @version 1.0.0
 */
public class WeikenException extends RuntimeException {

    private static final long serialVersionUID = 8919999818332018014L;

	public WeikenException(Exception e) {
        super(e);
    }

}
