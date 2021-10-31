package com.coumin.woowahancoupons.domain.coupon;

import com.coumin.woowahancoupons.domain.BaseEntity;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.Order;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionAlreadyAllocateCustomer;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_redemptions")
@Entity
public class CouponRedemption extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_redemption_id")
	private Long id;

	@Column(name = "issuance_code", nullable = false, unique = true)
	private UUID issuanceCode;

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
		this.expirationPeriod = ExpirationPeriod.from(coupon.getExpirationPolicy());
		this.coupon = coupon;
		this.issuanceCode = UUID.randomUUID();
	}

	public void allocateCustomer(Customer customer) {
		verifyCustomer();
		this.customer = customer;
	}

	public void verifyCustomer() {
		if (customer != null) {
			throw new CouponRedemptionAlreadyAllocateCustomer();
		}
	}
}
