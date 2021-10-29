package com.coumin.woowahancoupons.domain.coupon;

import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.Order;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_redemptions")
@Entity
public class CouponRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coupon_redemption_id")
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

    public CouponRedemption(Coupon coupon) {
        this.coupon = coupon;
    }

    public void changeCustomer(Customer customer) {
        this.customer = customer;
    }
}
