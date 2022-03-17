package com.pengsoft.support.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.pengsoft.support.util.IdentityCardUtils;

/**
 * The identity number constraint.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class IdentityNumberValidator implements ConstraintValidator<IdentityNumber, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return IdentityCardUtils.isValidCard(value);
    }

}
