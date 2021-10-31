package com.coumin.woowahancoupons.coupon.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
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
class CouponRedemptionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    private Customer testCustomer;

    private Coupon testCoupon;

    @BeforeEach
    public void beforeEach() {
        testCustomer = new Customer("test@gmail.com");
        testCoupon = TestCouponFactory.builder().build();
        entityManager.persist(testCustomer);
        entityManager.persist(testCoupon);
    }

    @Test
    @DisplayName("고객이 쿠폰 번호를 입력해 쿠폰 등록 - 성공 테스트")
    void registerCouponCodeSuccessTest() throws Exception {
        //given
        CouponRedemption couponRedemption = CouponRedemption.of(testCoupon);
        entityManager.persist(couponRedemption);
        //when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/coupons/{couponCode}/customers/{customerId}/register",
                couponRedemption.getCouponCode(),
                testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("registerCouponCode"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("고객이 쿠폰 번호를 입력해 쿠폰 등록 - 실패 테스트 (잘못된 쿠폰 번호)")
    void registerCouponCodeFailureTest() throws Exception {
        //given
        UUID wrongCouponCode = UUID.randomUUID();
        //when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/coupons/{couponCode}/customers/{customerId}/register",
                wrongCouponCode,
                testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("registerCouponCode"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getCode())))
            .andExpect(jsonPath("$.error.message", containsString(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getMessage())));
    }
}
