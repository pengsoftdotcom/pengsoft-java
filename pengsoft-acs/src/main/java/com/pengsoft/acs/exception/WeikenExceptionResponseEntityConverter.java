package com.pengsoft.acs.exception;

import java.util.Map;

import javax.inject.Named;

import com.pengsoft.support.exception.ExceptionResponseEntityConverter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Convert a {@link WeikenException} to a {@link ResponseEntity}
 * 
 * @author peng.dang@pengsoft.com
 * @version 1.0.0
 */
@Named
public class WeikenExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {

    @Override
    public boolean support(Throwable cause) {
        return cause instanceof WeikenException;
    }

    @Override
    public ResponseEntity<Object> convert(Throwable cause) {
        return new ResponseEntity<>(Map.of("result", 1, "success", false), HttpStatus.OK);
    }

}
