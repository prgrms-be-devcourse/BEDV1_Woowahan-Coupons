package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import com.coumin.woowahancoupons.global.exception.StoreNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SimpleCouponService implements CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;

    public SimpleCouponService(
        CouponRepository couponRepository,
        StoreRepository storeRepository
    ) {
        this.couponRepository = couponRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    @Override
    public void saveAllStoreCoupons(Long storeId, StoreCouponSaveRequestDto requestDto) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }

        List<Coupon> coupons = requestDto.getStoreCouponSaveDtos().stream()
            .map(storeCouponSaveDto -> storeCouponSaveDto.toEntity(storeId))
            .collect(Collectors.toCollection(ArrayList::new));
        couponRepository.saveAll(coupons);
    }
}
