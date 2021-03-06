package com.coumin.woowahancoupons.coupon.dto;

import com.coumin.woowahancoupons.coupon.validator.annotation.CouponAmountConstraint;
import com.coumin.woowahancoupons.coupon.validator.annotation.FixedAmountGroup;
import com.coumin.woowahancoupons.coupon.validator.annotation.PercentAmountGroup;
import java.time.LocalDateTime;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@CouponAmountConstraint
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCreateRequestDto {

    @Size(min = 1, max = 100)
    @NotBlank
    private String name;

    @Max(value = 10_000, groups = FixedAmountGroup.class)
    @Min(value = 1000, groups = FixedAmountGroup.class)
    @Max(value = 100, groups = PercentAmountGroup.class)
    @Min(value = 10, groups = PercentAmountGroup.class)
    private Long amount;

    @Max(100_000)
    @PositiveOrZero
    private Long minOrderPrice;

    @NotBlank
    private String discountType;

    @NotBlank
    private String issuerType;

    @NotNull
    private Long issuerId;

    @Max(10_000)
    @Min(1)
    private Integer maxCount;

    private Integer allocatedCount;

    @Max(5)
    @Min(1)
    private Integer maxCountPerCustomer;

    private String promotionCode;

    @NotBlank
    private String expirationPolicyType;

    private LocalDateTime startAt;

    @FutureOrPresent(message = "expiration date must be future or present.")
    private LocalDateTime expiredAt;

    @Max(365)
    @Min(0)
    private Integer daysFromIssuance;

    public CouponCreateRequestDto(
        String name, Long amount, Long minOrderPrice,
        String discountType, String issuerType, Long issuerId, Integer maxCount,
        Integer allocatedCount, Integer maxCountPerCustomer, String promotionCode,
        String expirationPolicyType, LocalDateTime startAt, LocalDateTime expiredAt,
        Integer daysFromIssuance
    ) {
        this.name = name;
        this.amount = amount;
        this.minOrderPrice = minOrderPrice;
        this.discountType = discountType;
        this.issuerType = issuerType;
        this.issuerId = issuerId;
        this.maxCount = maxCount;
        this.allocatedCount = allocatedCount;
        this.maxCountPerCustomer = maxCountPerCustomer;
        this.promotionCode = promotionCode;
        this.expirationPolicyType = expirationPolicyType;
        this.startAt = startAt;
        this.expiredAt = expiredAt;
        this.daysFromIssuance = daysFromIssuance;
    }
}
