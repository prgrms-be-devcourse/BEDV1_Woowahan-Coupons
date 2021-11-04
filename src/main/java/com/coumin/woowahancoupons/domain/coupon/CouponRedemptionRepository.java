package com.coumin.woowahancoupons.domain.coupon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {

    Optional<CouponRedemption> findByCouponCode(UUID couponCode);

    @EntityGraph(attributePaths = {"coupon"}, type = EntityGraphType.LOAD)
    List<CouponRedemption> findByCustomerIdAndUsedFalse(Long customerId);

    @Query("select count(cr) from CouponRedemption cr"
        + " where cr.coupon.id = :couponId and cr.customer.id = :customerId")
    int countByCouponIdAndCustomerId(@Param("couponId") Long couponId, @Param("customerId") Long customerId);
}
