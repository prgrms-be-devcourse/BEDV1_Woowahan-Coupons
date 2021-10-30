package com.coumin.woowahancoupons.coupon.dto;

import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponCreateResponseDto {

    private String name;

    private Long amount;

    private ExpirationPolicy expirationPolicy;

    private Long minOrderPrice;

    private String discountType;

    private String issuerType;

    private Long issuerId;

    private Integer maxCount;

    private Integer allocatedCount;

    private Integer maxCountPerCustomer;

    private String promotionCode;

    @Builder
    public CouponCreateResponseDto(String name, Long amount,
                                   ExpirationPolicy expirationPolicy, Long minOrderPrice, String discountType,
                                   String issuerType, Long issuerId, Integer maxCount, Integer allocatedCount,
                                   Integer maxCountPerCustomer, String promotionCode) {
        this.name = name;
        this.amount = amount;
        this.expirationPolicy = expirationPolicy;
        this.minOrderPrice = minOrderPrice;
        this.discountType = discountType;
        this.issuerType = issuerType;
        this.issuerId = issuerId;
        this.maxCount = maxCount;
        this.allocatedCount = allocatedCount;
        this.maxCountPerCustomer = maxCountPerCustomer;
        this.promotionCode = promotionCode;
    }
}
