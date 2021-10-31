package com.coumin.woowahancoupons.coupon.dto;

import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicyType;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import org.springframework.stereotype.Component;

@Component
public class CouponConverter {

    public Coupon convertToCoupon(CouponCreateRequestDto dto) {

        ExpirationPolicy expirationPolicy;
        if (ExpirationPolicyType.valueOf(dto.getExpirationPolicyType()) == ExpirationPolicyType.PERIOD) {
            expirationPolicy = ExpirationPolicy.newByPeriod(dto.getStartAt(), dto.getExpiredAt());
        } else {
            expirationPolicy = ExpirationPolicy.newByAfterIssueDate(dto.getDaysFromIssuance());
        }

        return Coupon.builder(
            dto.getName(),
            dto.getAmount(),
            expirationPolicy,
            DiscountType.valueOf(dto.getDiscountType()),
            IssuerType.valueOf(dto.getIssuerType()),
            dto.getIssuerId())
            .minOrderPrice(dto.getMinOrderPrice())
            .maxCount(dto.getMaxCount())
            .allocatedCount(dto.getAllocatedCount())
            .maxCountPerCustomer(dto.getMaxCountPerCustomer())
            .promotionCode(dto.getPromotionCode())
            .build();
    }

    public CouponCreateRequestDto convertToCouponCreateRequest(Coupon coupon) {
        return new CouponCreateRequestDto(
            coupon.getName(),
            coupon.getAmount(),
            coupon.getMinOrderPrice(),
            coupon.getDiscountType().name(),
            coupon.getIssuerType().name(),
            coupon.getIssuerId(),
            coupon.getMaxCount(),
            coupon.getAllocatedCount(),
            coupon.getMaxCountPerCustomer(),
            coupon.getPromotionCode(),
            coupon.getExpirationPolicy().getExpirationPolicyType().name(),
            coupon.getExpirationPolicy().getStartAt(),
            coupon.getExpirationPolicy().getExpiredAt(),
            coupon.getExpirationPolicy().getDaysFromIssuance()
        );
    }

    public CouponCreateResponseDto convertToCouponCreateResponse(Coupon coupon) {
        return CouponCreateResponseDto.builder()
            .name(coupon.getName())
            .amount(coupon.getAmount())
            .expirationPolicy(coupon.getExpirationPolicy())
            .minOrderPrice(coupon.getMinOrderPrice())
            .discountType(coupon.getDiscountType().name())
            .issuerType(coupon.getIssuerType().name())
            .issuerId(coupon.getIssuerId())
            .maxCount(coupon.getMaxCount())
            .allocatedCount(coupon.getAllocatedCount())
            .maxCountPerCustomer(coupon.getMaxCountPerCustomer())
            .promotionCode(coupon.getPromotionCode())
            .build();
    }
}
