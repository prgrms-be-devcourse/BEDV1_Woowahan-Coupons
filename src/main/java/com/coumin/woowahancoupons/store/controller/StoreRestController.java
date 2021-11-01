package com.coumin.woowahancoupons.store.controller;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponResponseDto;
import com.coumin.woowahancoupons.global.ApiResponse;
import com.coumin.woowahancoupons.store.service.StoreService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/stores")
@RestController
public class StoreRestController {

    private final StoreService storeService;

    public StoreRestController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{storeId}/coupons")
    public ApiResponse<List<StoreCouponResponseDto>> getStoreCoupons(@PathVariable long storeId) {
        List<StoreCouponResponseDto> responseDtoList = storeService.findStoreCoupons(storeId);
        return ApiResponse.success(responseDtoList);
    }
}
