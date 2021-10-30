package com.coumin.woowahancoupons.domain.coupon;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, UUID> {

	Optional<CouponRedemption> findByIssuanceCode(UUID issuanceCode);

}
