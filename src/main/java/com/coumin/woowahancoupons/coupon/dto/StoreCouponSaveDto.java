package com.coumin.woowahancoupons.coupon.dto;


import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCouponSaveDto {

	@Size(min = 2, max = 100)
	@NotEmpty
	private String name;


	@Max(10000)
	@Min(1000)
	@NotNull
	private Long amount;

	@Max(30)
	@Positive
	@NotNull
	private Integer daysAfterIssuance;

	@Positive
	@NotNull
	private Long minOrderPrice;

	@Builder
	public StoreCouponSaveDto(
		String name,
		Long amount,
		Integer daysAfterIssuance,
		Long minOrderPrice) {
		this.name = name;
		this.amount = amount;
		this.daysAfterIssuance = daysAfterIssuance;
		this.minOrderPrice = minOrderPrice;
	}

	public Coupon toEntity(Long issuerId) {
		ExpirationPolicy expirationPolicy = ExpirationPolicy.newByAfterIssueDate(daysAfterIssuance);

		return Coupon.builder(
			name,
			amount,
			expirationPolicy,
			DiscountType.FIXED_AMOUNT,
			IssuerType.STORE,
			issuerId)
			.minOrderPrice(minOrderPrice)
			.maxCountPerCustomer(1)
			.build();
	}
}
