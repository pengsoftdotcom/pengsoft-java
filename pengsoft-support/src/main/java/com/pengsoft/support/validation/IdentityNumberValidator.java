package com.pengsoft.support.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.pengsoft.support.util.IdentityCardUtils;
import com.pengsoft.support.util.StringUtils;

/**
 * The identity number constraint.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class IdentityNumberValidator implements ConstraintValidator<IdentityNumber, String> {

    private boolean allowBlank = false;

    @Override
    public void initialize(IdentityNumber annotation) {
        allowBlank = annotation.allowBlank();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (allowBlank && StringUtils.isBlank(value)) {
            return true;
        }
        return IdentityCardUtils.isValidCard(value);
    }

}
