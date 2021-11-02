package com.coumin.woowahancoupons.store.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import com.coumin.woowahancoupons.domain.store.Store;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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
        List<Coupon> storeCoupons = LongStream.range(0, 3)
            .mapToObj(i -> TestCouponFactory.builder()
                .name("store coupon#" + i)
                .amount(1000 * (i + 1))
                .expirationPolicy(ExpirationPolicy.newByAfterIssueDate(14))
                .discountType(DiscountType.FIXED_AMOUNT)
                .minOrderPrice(1000 * (i + 1))
                .issuerType(IssuerType.STORE)
                .issuerId(storeId)
                .build())
            .collect(Collectors.toList());
        couponRepository.saveAll(storeCoupons);

        //When
        ResultActions resultActions = requestGetStoreCoupons(storeId);

        //Then
        resultActions
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(StoreRestController.class))
            .andExpect(handler().methodName("getStoreCoupons"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(storeCoupons.size())))
            .andExpect(jsonPath("$.data[0].id", is(storeCoupons.get(0).getId().intValue())))
            .andExpect(jsonPath("$.data[0].name", is(storeCoupons.get(0).getName())))
            .andExpect(jsonPath("$.data[0].amount", is(storeCoupons.get(0).getAmount().intValue())))
            .andExpect(jsonPath("$.data[0].minOrderPrice", is(storeCoupons.get(0).getMinOrderPrice().intValue())))
            .andExpect(jsonPath("$.data[0].daysAfterIssuance", is(storeCoupons.get(0).getExpirationPolicy().getDaysFromIssuance())))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    private ResultActions requestGetStoreCoupons(long storeId) throws Exception {
        return mockMvc.perform(get("/api/v1/stores/{storeId}/coupons", storeId)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }
}
