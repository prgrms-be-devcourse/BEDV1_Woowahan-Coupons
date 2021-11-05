package com.coumin.woowahancoupons.coupon.validator;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.validator.annotation.CouponAmountConstraint;
import com.coumin.woowahancoupons.coupon.validator.annotation.FixedAmountGroup;
import com.coumin.woowahancoupons.coupon.validator.annotation.PercentAmountGroup;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class CouponAmountConstraintValidator implements
    ConstraintValidator<CouponAmountConstraint, CouponCreateRequestDto> {

    private final Validator validator;

    public CouponAmountConstraintValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(CouponCreateRequestDto value, ConstraintValidatorContext context) {
        final Set<ConstraintViolation<Object>> constraintViolations = getConstraintViolations(value);

        if (!constraintViolations.isEmpty()) {
            context.disableDefaultConstraintViolation();
            constraintViolations
                .forEach(constraintViolation -> context
                    .buildConstraintViolationWithTemplate(constraintViolation.getMessage())
                    .addConstraintViolation());
            return false;
        }
        return true;
    }

    private  Set<ConstraintViolation<Object>> getConstraintViolations(CouponCreateRequestDto value) {
        if (DiscountType.valueOf(value.getDiscountType()) == DiscountType.FIXED_AMOUNT) {
            return validator.validate(value, FixedAmountGroup.class);
        }
        if (DiscountType.valueOf(value.getDiscountType()) == DiscountType.PERCENT_DISCOUNT) {
            return validator.validate(value, PercentAmountGroup.class);
        }
        return Collections.emptySet();
    }
}
