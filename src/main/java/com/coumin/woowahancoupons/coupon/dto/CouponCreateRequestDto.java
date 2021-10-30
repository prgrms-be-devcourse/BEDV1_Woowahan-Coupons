package com.coumin.woowahancoupons.coupon.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCreateRequestDto {

    private String name;

    private Long amount;

    private Long minOrderPrice;

    private String discountType;

    private String issuerType;

    private Long issuerId;

    private Integer maxCount;

    private Integer allocatedCount;

    private Integer maxCountPerCustomer;

    private String promotionCode;

    private String expirationPolicyType;

    private LocalDateTime startAt;

    private LocalDateTime expiredAt;

    private Integer daysFromIssuance;

    @Builder
    public CouponCreateRequestDto(String name, Long amount, Long minOrderPrice, String discountType,
                                  String issuerType, Long issuerId, Integer maxCount, Integer allocatedCount,
                                  Integer maxCountPerCustomer, String promotionCode, String expirationPolicyType,
                                  LocalDateTime startAt, LocalDateTime expiredAt, Integer daysFromIssuance) {
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
