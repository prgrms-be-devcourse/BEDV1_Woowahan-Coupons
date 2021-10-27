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

    @Column(name = "max_cnt_per_cus")
    private Integer maxCountPerCustomer;

    @Column(name = "promotion_code")
    private String promotionCode;

    @OneToMany(mappedBy = "coupon")
    private List<CouponWallet> couponWallets = new ArrayList<>();
}
