package com.coumin.woowahancoupons.store.service;

import java.util.List;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponResponseDto;

public interface StoreService {

	List<StoreCouponResponseDto> findStoreCoupons(Long storeId);
}
