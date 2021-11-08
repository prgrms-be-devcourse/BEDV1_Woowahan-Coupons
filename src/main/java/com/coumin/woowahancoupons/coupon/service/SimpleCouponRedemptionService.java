package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.converter.CouponRedemptionConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponCheckRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponRedemptionResponseDto;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemptionRepository;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.domain.store.Brand;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import com.coumin.woowahancoupons.global.exception.CouponMaxCountOverException;
import com.coumin.woowahancoupons.global.exception.CouponMaxCountPerCustomerOverException;
import com.coumin.woowahancoupons.global.exception.CouponNotFoundException;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionNotFoundException;
import com.coumin.woowahancoupons.global.exception.CustomerNotFoundException;
import com.coumin.woowahancoupons.global.exception.StoreNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SimpleCouponRedemptionService implements CouponRedemptionService {

    private final CouponRedemptionRepository couponRedemptionRepository;

    private final CustomerRepository customerRepository;

    private final CouponRepository couponRepository;

    private final StoreRepository storeRepository;

    private final CouponRedemptionConverter couponRedemptionConverter;

    public SimpleCouponRedemptionService(
        CouponRedemptionRepository couponRedemptionRepository,
        CustomerRepository customerRepository,
        CouponRepository couponRepository,
        StoreRepository storeRepository,
        CouponRedemptionConverter couponRedemptionConverter
    ) {
        this.couponRedemptionRepository = couponRedemptionRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
        this.storeRepository = storeRepository;
        this.couponRedemptionConverter = couponRedemptionConverter;
    }

    @Transactional
    @Override
    public void allocateExistingCouponToCustomer(UUID couponCode, Long customerId) {
        CouponRedemption couponRedemption = couponRedemptionRepository.findByCouponCode(couponCode)
            .orElseThrow(() -> new CouponRedemptionNotFoundException(couponCode));
        Customer customer = customerRepository.getById(customerId);
        couponRedemption.allocateCustomer(customer);
    }

    @Transactional
    @Override
    public void allocateCouponToCustomerWithIssuance(Long couponId, Long customerId) {
        int issuanceCount = 1;
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new CouponNotFoundException(couponId));
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
        if (!coupon.canIssueCouponCodes(issuanceCount)) {
            throw new CouponMaxCountOverException(coupon.getMaxCount(), coupon.getAllocatedCount(), issuanceCount);
        }
        int customerCouponCount = couponRedemptionRepository.countByCouponIdAndCustomerId(couponId, customerId);
        if (!coupon.canIssueCouponCodeToCustomer(customerCouponCount)) {
            throw new CouponMaxCountPerCustomerOverException(coupon.getMaxCountPerCustomer());
        }
        couponRedemptionRepository.save(CouponRedemption.of(coupon, customer));
        coupon.increaseAllocatedCount(issuanceCount);
    }

    @Override
    public List<CouponRedemptionResponseDto> findCustomerCouponRedemptions(Long customerId) {
        return couponRedemptionRepository.findByCustomerIdAndUsedFalse(customerId).stream()
            .filter(Predicate.not(CouponRedemption::isExpiration))
            .map(couponRedemptionConverter::convertToCouponRedemptionResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void issueCouponCodes(Long couponId, int issuanceCount) {
        Coupon coupon = couponRepository.findByIdForUpdate(couponId)
            .orElseThrow(() -> new CouponNotFoundException(couponId));
        if (!coupon.canIssueCouponCodes(issuanceCount)) {
            throw new CouponMaxCountOverException(coupon.getMaxCount(), coupon.getAllocatedCount(),
                issuanceCount);
        }
        List<CouponRedemption> couponRedemptions = IntStream.rangeClosed(1, issuanceCount)
            .mapToObj(operand -> CouponRedemption.of(coupon))
            .collect(Collectors.toList());
        int insertSize = couponRedemptionRepository.saveAll(couponRedemptions).size();
        coupon.increaseAllocatedCount(insertSize);
    }

    @Override
    public void checkCouponForUse(Long couponRedemptionId, CouponCheckRequestDto couponCheckRequestDto) {
        CouponRedemption couponRedemption = couponRedemptionRepository.findById(couponRedemptionId)
            .orElseThrow(() -> new CouponRedemptionNotFoundException(couponRedemptionId));

        Long storeId = couponCheckRequestDto.getStoreId();
        Long issuerId = storeId;
        if (couponRedemption.isBrandCouponRedemption()) {
            Brand brand = storeRepository.findById(storeId).orElseThrow(
                () -> new StoreNotFoundException(storeId)
            ).getBrand();
            issuerId = brand.getId();
        }
        couponRedemption.verifyForUse(issuerId, couponCheckRequestDto.getOrderPrice());
    }

    @Transactional
    @Override
    public void useCustomerCoupon(Long couponRedemptionId) {
        CouponRedemption couponRedemption = couponRedemptionRepository.findById(couponRedemptionId)
            .orElseThrow(() -> new CouponRedemptionNotFoundException(couponRedemptionId));
        couponRedemption.use();
    }
}
