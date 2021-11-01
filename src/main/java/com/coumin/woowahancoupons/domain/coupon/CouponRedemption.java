package com.coumin.woowahancoupons.domain.coupon;

import com.coumin.woowahancoupons.domain.BaseEntity;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.Order;
import com.coumin.woowahancoupons.global.exception.CouponAlreadyUseException;
import com.coumin.woowahancoupons.global.exception.CouponExpireException;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionAlreadyAllocateCustomer;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.util.Assert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_redemptions")
@Entity
public class CouponRedemption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_redemption_id")
    private Long id;

    @Column(name = "coupon_code", nullable = false, unique = true)
    private UUID couponCode;

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

    private CouponRedemption(Coupon coupon, Customer customer) {
        Assert.notNull(coupon, "coupon must be not null");

        this.coupon = coupon;
        this.customer = customer;
        this.expirationPeriod = coupon.getExpirationPolicy().newExpirationPeriod();
        this.couponCode = UUID.randomUUID();
    }

    public static CouponRedemption of(Coupon coupon) {
        return new CouponRedemption(coupon, null);
    }

    public static CouponRedemption of(Coupon coupon, Customer customer) {
        Assert.notNull(customer, "customer must be not null");

        return new CouponRedemption(coupon, customer);
    }

    public void allocateCustomer(Customer customer) {
        verifyCustomer();
        this.customer = customer;
    }

    public void use() {
        verifyExpiration();
        verifyUsed();
        this.used = true;
        this.usedAt = LocalDateTime.now();
    }

    public void verifyCustomer() {
        if (customer != null) {
            throw new CouponRedemptionAlreadyAllocateCustomer();
        }
    }

    private void verifyExpiration() {
        if (expirationPeriod.isExpiration()) {
            throw new CouponExpireException();
        }
    }

    private void verifyUsed() {
        if (used) {
            throw new CouponAlreadyUseException();
        }
    }
}
