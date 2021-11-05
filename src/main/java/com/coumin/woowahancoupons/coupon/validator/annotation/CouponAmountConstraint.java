package com.coumin.woowahancoupons.coupon.validator.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.coumin.woowahancoupons.coupon.validator.CouponAmountConstraintValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@Constraint(validatedBy = CouponAmountConstraintValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface CouponAmountConstraint {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
