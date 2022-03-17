package com.pengsoft.support.exception;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Convert a {@link FileSizeLimitExceededException} to a {@link ResponseEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class FileSizeLimitExceededExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {

    @Inject
    private MessageSource messageSource;

    @Override
    public boolean support(Throwable cause) {
        return cause instanceof FileSizeLimitExceededException;
    }

    @Override
    public ResponseEntity<Object> convert(Throwable cause) {
        final var e = (FileSizeLimitExceededException) cause;
        final var message = messageSource.getMessage("file_size_limit_exceeded",
                new Object[] { e.getPermittedSize() / 1024 / 1024 }, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(Map.of("error", "file_size_limit_exceeded", "error_description", message),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
