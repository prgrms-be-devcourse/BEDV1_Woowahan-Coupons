package com.coumin.woowahancoupons.domain.coupon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {

    Optional<CouponRedemption> findByCouponCode(UUID couponCode);

    @EntityGraph(attributePaths = {"coupon"}, type = EntityGraphType.LOAD)
    List<CouponRedemption> findByCustomerIdAndUsedFalse(Long customerId);
}
