package com.coumin.woowahancoupons.domain.coupon;

import com.coumin.woowahancoupons.domain.BaseEntity;
import lombok.*;
import javax.persistence.*;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons", indexes = @Index(name = "coupon_idx", columnList = "issuer_id"))
@Entity
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Embedded
    private ExpirationPolicy expirationPolicy;

    @Column(name = "min_order_price")
    private Long minOrderPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 30)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "issuer_type", nullable = false, length = 30)
    private IssuerType issuerType;

    @Column(name = "issuer_id", nullable = false)
    private Long issuerId;

    @Column(name = "max_cnt")
    private Integer maxCount;

    @Column(name = "allocated_cnt")
    private Integer allocatedCount;

    @Column(name = "max_cnt_per_cus")
    private Integer maxCountPerCustomer;

    @Column(name = "promotion_code")
    private String promotionCode;

    @OneToMany(mappedBy = "coupon")
    private final List<CouponRedemption> couponRedemptions = new ArrayList<>();

    @Builder(builderMethodName = "internalBuilder")
    private Coupon(Long id, String name, Long amount,
        ExpirationPolicy expirationPolicy, Long minOrderPrice,
        DiscountType discountType, IssuerType issuerType, Long issuerId, Integer maxCount,
        Integer allocatedCount, Integer maxCountPerCustomer, String promotionCode) {
        this.id = id;
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

    public static CouponBuilder builder(
        String name,
        Long amount,
        ExpirationPolicy expirationPolicy,
        DiscountType discountType,
        IssuerType issuerType,
        Long issuerId
    ) {
        Objects.requireNonNull(name, "name must not be null!");
        Objects.requireNonNull(amount, "amount must not be null!");
        Objects.requireNonNull(expirationPolicy, "expirationPolicy must not be null!");
        Objects.requireNonNull(discountType, "discountType must not be null!");
        Objects.requireNonNull(issuerType, "issuerType must not be null!");
        Objects.requireNonNull(issuerId, "issuerId must not be null!");

        return internalBuilder()
            .name(name)
            .amount(amount)
            .expirationPolicy(expirationPolicy)
            .discountType(discountType)
            .issuerType(issuerType)
            .issuerId(issuerId);
    }

}
