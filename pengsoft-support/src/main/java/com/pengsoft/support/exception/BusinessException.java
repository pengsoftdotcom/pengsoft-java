package com.pengsoft.support.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * The business exception.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7279199575529512379L;

	private final String code;

    private final transient Object[] args;

    private final HttpStatus status;

    public BusinessException(final String code, final Object... args) {
        this(code, HttpStatus.BAD_REQUEST, args);
    }

    public BusinessException(final String code, HttpStatus status, final Object... args) {
        super(code);
        this.code = code;
        this.status = status;
        this.args = args;
    }
}
