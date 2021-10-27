package com.coumin.woowahancoupons.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_wallet")
@Entity
public class CouponWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coupon_wallet_id")
    private UUID id;

    @Column(name = "use_yn", nullable = false)
    private boolean used;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime usedAt;

    @Embedded
    private ExpirationPeriod expirationPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public CouponWallet(Coupon coupon) {
        this.coupon = coupon;
    }

    public void changeCustomer(Customer customer) {
        this.customer = customer;
    }
}
