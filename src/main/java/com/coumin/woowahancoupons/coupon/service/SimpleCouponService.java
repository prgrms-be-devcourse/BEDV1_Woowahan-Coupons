package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.domain.Coupon;
import com.coumin.woowahancoupons.domain.CouponRepository;
import com.coumin.woowahancoupons.domain.StoreRepository;
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
    public void saveAllStoreCoupons(Long storeId, List<StoreCouponSaveRequestDto> requestDtoList) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(String.format("store(%d)", storeId));
        }

        ArrayList<Coupon> coupons = requestDtoList.stream()
            .map(requestDto -> requestDto.toEntity(storeId))
            .collect(Collectors.toCollection(ArrayList::new));
        couponRepository.saveAll(coupons);
    }
}
