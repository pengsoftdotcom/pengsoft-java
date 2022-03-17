package com.pengsoft.support.exception;

import static com.pengsoft.support.util.StringUtils.PACKAGE_SEPARATOR;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import com.pengsoft.support.util.StringUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert a {@link ConstraintViolationException} to a {@link ResponseEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@Named
public class ConstraintViolationExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {

    @Override
    public boolean support(final Throwable cause) {
        return cause instanceof ConstraintViolationException;
    }

    @Override
    public ResponseEntity<Object> convert(final Throwable cause) {
        final var e = (ConstraintViolationException) cause;
        final var body = e.getConstraintViolations().stream().collect(Collectors.toMap(cv -> {
            var propertyPath = cv.getPropertyPath().toString();
            if (propertyPath.contains(PACKAGE_SEPARATOR)) {
                propertyPath = StringUtils.substringAfter(propertyPath, PACKAGE_SEPARATOR);
                if (propertyPath.contains(PACKAGE_SEPARATOR)) {
                    propertyPath = StringUtils.substringAfter(propertyPath, PACKAGE_SEPARATOR);
                }
            }
            return propertyPath;
        }, cv -> List.of(cv.getMessage()), (final List<String> oldList, final List<String> newList) -> {
            final var result = new ArrayList<String>();
            result.addAll(oldList);
            result.addAll(newList);
            return result;
        }));
        log.debug("Validation failed.", e);
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
