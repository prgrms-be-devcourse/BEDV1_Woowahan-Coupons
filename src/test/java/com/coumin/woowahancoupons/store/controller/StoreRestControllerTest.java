package com.coumin.woowahancoupons.store.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import com.coumin.woowahancoupons.domain.store.Store;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class StoreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("매장의 쿠폰 리스트 조회 요청 성공")
    void findStoreCouponsTest() throws Exception {
        //Given
        long storeId = storeRepository.save(new Store("testStore#1")).getId();
        List<Coupon> storeCoupons = IntStream.range(0, 3)
            .mapToObj(i -> TestCouponFactory.builder()
                .id((long) i)
                .name("store coupon#" + i)
                .amount(1000L * (i + 1))
                .expirationPolicy(ExpirationPolicy.newByAfterIssueDate(14))
                .discountType(DiscountType.FIXED_AMOUNT)
                .issuerType(IssuerType.STORE)
                .issuerId(storeId)
                .build())
            .collect(Collectors.toList());
        couponRepository.saveAll(storeCoupons);

        //When
        ResultActions resultActions = requestGetStoreCoupons(storeId);

        //Then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(StoreRestController.class))
            .andExpect(handler().methodName("getStoreCoupons"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", is(notNullValue())))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    private ResultActions requestGetStoreCoupons(long storeId) throws Exception {
        return mockMvc.perform(get("/api/v1/stores/{storeId}/coupons", storeId)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }
}
