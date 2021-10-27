package com.coumin.woowahancoupons.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons")
@Entity
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Long amount;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiredAt;

    private Long minOrderPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IssuerType issuerType;

    @Column(nullable = false)
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
    private final List<CouponWallet> couponWallets = new ArrayList<>();

    @Builder(builderMethodName = "internalBuilder")
    private Coupon(Long id, String name, Long amount, LocalDateTime startAt,
        LocalDateTime expiredAt, Long minOrderPrice,
        DiscountType discountType, IssuerType issuerType, Long issuerId, Integer maxCount,
        Integer allocatedCount, Integer maxCountPerCustomer, String promotionCode) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.startAt = startAt;
        this.expiredAt = expiredAt;
        this.minOrderPrice = minOrderPrice;
        this.discountType = discountType;
        this.issuerType = issuerType;
        this.issuerId = issuerId;
        this.maxCount = maxCount;
        this.allocatedCount = allocatedCount;
        this.maxCountPerCustomer = maxCountPerCustomer;
        this.promotionCode = promotionCode;
    }

    public static CouponBuilder builder(String name, Long amount, LocalDateTime expiredAt,
        DiscountType discountType, IssuerType issuerType, Long issuerId) {

        Objects.requireNonNull(name, "name must not be null!");
        Objects.requireNonNull(amount, "amount must not be null!");
        Objects.requireNonNull(expiredAt, "expiredAt must not be null!");
        Objects.requireNonNull(discountType, "discountType must not be null!");
        Objects.requireNonNull(issuerType, "issuerType must not be null!");
        Objects.requireNonNull(issuerId, "issuerId must not be null!");

        return internalBuilder()
            .name(name)
            .amount(amount)
            .expiredAt(expiredAt)
            .discountType(discountType)
            .issuerType(issuerType)
            .issuerId(issuerId);
    }
}
