package com.pengsoft.support.exception;

/**
 * If this exception occurs, it means that some necessary data or configuration
 * is invalid.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class InvalidConfigurationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3160109454213486927L;

	public InvalidConfigurationException(final String message) {
        super(message);
    }

}
