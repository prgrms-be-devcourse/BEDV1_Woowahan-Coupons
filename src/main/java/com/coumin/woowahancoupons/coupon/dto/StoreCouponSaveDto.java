package com.coumin.woowahancoupons.coupon.dto;


import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicyType;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCouponSaveDto {

    private String name;

    private Long amount;

    private Integer daysAfterIssuance;

    private Long minOrderPrice;

    @Builder
    public StoreCouponSaveDto(
        String name,
        Long amount,
        Integer daysAfterIssuance,
        Long minOrderPrice) {
        this.name = name;
        this.amount = amount;
        this.daysAfterIssuance = daysAfterIssuance;
        this.minOrderPrice = minOrderPrice;
    }

    public Coupon toEntity(Long issuerId) {
        ExpirationPolicy expirationPolicy = ExpirationPolicy.ByAfterIssueDateTypeBuilder()
            .expirationPolicyType(ExpirationPolicyType.AFTER_ISSUE_DATE)
            .daysFromIssuance(daysAfterIssuance)
            .build();

        return Coupon.builder(
            name,
            amount,
            expirationPolicy,
            DiscountType.FIXED_AMOUNT,
            IssuerType.STORE,
            issuerId)
            .minOrderPrice(minOrderPrice)
            .maxCountPerCustomer(1)
            .build();
    }
}
