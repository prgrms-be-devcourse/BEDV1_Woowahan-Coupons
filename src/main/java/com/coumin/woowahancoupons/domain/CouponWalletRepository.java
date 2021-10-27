package com.coumin.woowahancoupons.domain;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponWalletRepository extends JpaRepository<CouponWallet, UUID> {

}
