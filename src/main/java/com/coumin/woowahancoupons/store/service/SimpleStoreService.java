package com.coumin.woowahancoupons.store.service;

import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponResponseDto;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SimpleStoreService implements StoreService {

	private final CouponRepository couponRepository;

	public SimpleStoreService(
		CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	@Override
	public List<StoreCouponResponseDto> findStoreCoupons(Long storeId) {
		return couponRepository.findByIssuerId(storeId).stream()
			.map(StoreCouponResponseDto::new)
			.collect(Collectors.toList());
	}
}
