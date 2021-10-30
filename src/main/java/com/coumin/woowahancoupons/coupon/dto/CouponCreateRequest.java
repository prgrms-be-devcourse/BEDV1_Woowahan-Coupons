package com.coumin.woowahancoupons.coupon.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequest {

    private String name;
    private Long amount;
    private Long minOrderPrice;
    private String discountType;
    private String issuerType;
    private Long issuerId;
    private Integer maxCount;
    private Integer allocatedCount;
    private Integer maxCountPerCustomer;
    private String promotionCod;
    private String expirationPolicyType;
    private LocalDateTime startAt;
    private LocalDateTime expiredAt;
    private Integer daysFromIssuance;
}
