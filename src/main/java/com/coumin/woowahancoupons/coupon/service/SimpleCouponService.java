package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.converter.CouponConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponAdminRepository;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import com.coumin.woowahancoupons.global.exception.AdminNotFoundException;
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
    private final CouponConverter couponConverter;
    private final CouponAdminRepository couponAdminRepository;

    public SimpleCouponService(
        CouponRepository couponRepository,
        StoreRepository storeRepository,
        CouponConverter couponConverter,
        CouponAdminRepository couponAdminRepository) {
        this.couponRepository = couponRepository;
        this.storeRepository = storeRepository;
        this.couponConverter = couponConverter;
        this.couponAdminRepository = couponAdminRepository;
    }

    @Transactional
    @Override
    public void saveAllStoreCoupons(Long storeId, StoreCouponSaveRequestDto requestDto) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }

        List<Coupon> coupons = requestDto.getStoreCouponSaves().stream()
            .map(storeCouponSaveDto -> storeCouponSaveDto.toEntity(storeId))
            .collect(Collectors.toCollection(ArrayList::new));
        couponRepository.saveAll(coupons);
    }

    @Transactional
    @Override
    public CouponCreateResponseDto generateCoupon(CouponCreateRequestDto couponCreateRequest,
                                                  Long couponAdminId) {
        if (!couponAdminRepository.existsById(couponAdminId)) {
            throw new AdminNotFoundException(couponAdminId);
        }

        Coupon coupon = couponConverter.convertToCoupon(couponCreateRequest);
        Coupon saveToCoupon = couponRepository.save(coupon);
        return couponConverter.convertToCouponCreateResponse(saveToCoupon);
    }
}
