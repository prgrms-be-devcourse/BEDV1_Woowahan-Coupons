package com.coumin.woowahancoupons.coupon.dto;

import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicyType;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateConverter {

    public Coupon convertToCoupon(CouponCreateRequest dto) {

        ExpirationPolicy expirationPolicy;

        if (ExpirationPolicyType.valueOf(dto.getExpirationPolicyType()) == ExpirationPolicyType.PERIOD) {
            expirationPolicy = ExpirationPolicy.newByPeriod(dto.getStartAt(), dto.getExpiredAt());
        } else {
            expirationPolicy = ExpirationPolicy.newByAfterIssueDate(dto.getDaysFromIssuance());
        }

        return Coupon.internalBuilder()
            .name(dto.getName())
            .amount(dto.getAmount())
            .expirationPolicy(expirationPolicy)
            .minOrderPrice(dto.getMinOrderPrice())
            .discountType(DiscountType.valueOf(dto.getDiscountType()))
            .issuerType(IssuerType.valueOf(dto.getIssuerType()))
            .issuerId(dto.getIssuerId())
            .maxCount(dto.getMaxCount())
            .allocatedCount(dto.getAllocatedCount())
            .maxCountPerCustomer(dto.getMaxCountPerCustomer())
            .promotionCode(dto.getPromotionCod())
            .build();

    }

    public CouponCreateResponse convertToCouponCreateResponse(Coupon coupon) {
        return CouponCreateResponse.builder()
            .name(coupon.getName())
            .amount(coupon.getAmount())
//            .expirationPolicy(ExpirationPolicyType.valueOf(coupon.getExpirationPolicyType())
//                .createExpirationPolicy(coupon.getStartAt(), coupon.getExpiredAt(), coupon.getDaysFromIssuance()))
            .minOrderPrice(coupon.getMinOrderPrice())
//            .discountType(DiscountType.valueOf(coupon.getDiscountType()))
//            .issuerType(IssuerType.valueOf(coupon.getIssuerType()))
            .issuerId(coupon.getIssuerId())
            .maxCount(coupon.getMaxCount())
            .allocatedCount(coupon.getAllocatedCount())
            .maxCountPerCustomer(coupon.getMaxCountPerCustomer())
//            .promotionCode(coupon.getPromotionCod())
            .build();
    }

}
