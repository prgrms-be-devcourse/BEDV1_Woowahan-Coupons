package com.coumin.woowahancoupons.coupon.dto;

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
public class CouponCreateResponse {

    private String name;
    private Long amount;
    private String expirationPolicy;
    private Long minOrderPrice;
    private String discountType;
    private String issuerType;
    private Long issuerId;
    private Integer maxCount;
    private Integer allocatedCount;
    private Integer maxCountPerCustomer;
    private String promotionCod;


}
