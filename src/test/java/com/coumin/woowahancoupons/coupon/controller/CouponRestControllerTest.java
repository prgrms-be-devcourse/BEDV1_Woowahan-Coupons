package com.coumin.woowahancoupons.coupon.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.coupon.converter.CouponConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.CouponAdmin;
import com.coumin.woowahancoupons.domain.coupon.CouponAdminRepository;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import com.coumin.woowahancoupons.domain.store.Store;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class CouponRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CouponAdminRepository couponAdminRepository;

    @Autowired
    private CouponConverter couponConverter;

    @Test
    @DisplayName("매장의 쿠폰 생성 성공")
    void createStoreCouponsSuccessTest() throws Exception {
        //Given
        long storeId = storeRepository.save(new Store("testStore#1")).getId();
        List<StoreCouponSaveDto> storeCouponSaveDtos = IntStream.range(0, 3)
            .mapToObj(i -> StoreCouponSaveDto.builder()
                .name("name#" + i)
                .amount(1000L * (i + 1))
                .daysAfterIssuance(30)
                .minOrderPrice(1000L * (i + 1))
                .build())
            .collect(Collectors.toList());
        StoreCouponSaveRequestDto requestDto = new StoreCouponSaveRequestDto(storeCouponSaveDtos);

        //When
        ResultActions resultActions = requestCreateStoreCoupons(storeId, requestDto);

        //TODO rest docs
        //Then
        resultActions
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("매장의 쿠폰 생성 실패 - 생성 수량 초과 및 쿠폰의 값들 0")
    void createStoreCouponsInvalidRequestValueTest() throws Exception {
        //Given
        long storeId = storeRepository.save(new Store("testStore#1")).getId();
        List<StoreCouponSaveDto> storeCouponSaveDtos = IntStream.range(0, 5)
            .mapToObj(i -> StoreCouponSaveDto.builder()
                .name("name#" + i)
                .amount(0L)
                .daysAfterIssuance(0)
                .minOrderPrice(0L)
                .build())
            .collect(Collectors.toList());
        StoreCouponSaveRequestDto requestDto = new StoreCouponSaveRequestDto(storeCouponSaveDtos);

        //When
        ResultActions resultActions = requestCreateStoreCoupons(storeId, requestDto);

        //TODO rest docs
        //Then
        resultActions
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("매장의 쿠폰 생성 실패 - post 대신 put 요청")
    void createStoreCouponsInvalidRequestMethodTest() throws Exception {
        //Given
        long storeId = storeRepository.save(new Store("testStore#1")).getId();
        List<StoreCouponSaveDto> storeCouponSaveDtos = IntStream.range(0, 5)
            .mapToObj(i -> StoreCouponSaveDto.builder()
                .name("name#" + i)
                .amount(1000L * (i + 1))
                .daysAfterIssuance(30)
                .minOrderPrice(1000L * (i + 1))
                .build())
            .collect(Collectors.toList());
        StoreCouponSaveRequestDto requestDto = new StoreCouponSaveRequestDto(storeCouponSaveDtos);

        //When
        ResultActions resultActions = mvc.perform(put("/api/v1/coupons/{storeId}/issuance", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andDo(print());

        //TODO rest docs
        //Then
        resultActions
            .andExpect(status().isMethodNotAllowed())
            .andDo(print());
    }

    @Test
    @DisplayName("관리자가 쿠폰을 발행 - 테스트 성공")
    void createAdminCouponSuccessTest() throws Exception {
        //Given
        Long couponAdminId = couponAdminRepository.save(new CouponAdmin("Admin_WOOCOU")).getId();

        CouponCreateRequestDto couponCreateRequestDto = couponConverter
            .convertToCouponCreateRequest(
                TestCouponFactory.builder()
                    .name("기리보이가 쏜다")
                    .amount(10_000L)
                    .minOrderPrice(10_000L)
                    .maxCount(10)
                    .allocatedCount(10)
                    .promotionCode("프로모션코드")
                    .build()
            );

        //When
        ResultActions resultActions = mvc.perform(
            post("/api/v1/coupons/{couponAdminId}", couponAdminId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponCreateRequestDto)))
            .andDo(print());

        //Then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRestController.class))
            .andExpect(handler().methodName("createAdminCoupon"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.name", is("기리보이가 쏜다")))
            .andExpect(jsonPath("$.data.amount", is(10_000)))
            .andExpect(jsonPath("$.data.minOrderPrice", is(10_000)))
            .andExpect(jsonPath("$.data.maxCount", is(10)))
            .andExpect(jsonPath("$.data.allocatedCount", is(10)))
            .andExpect(jsonPath("$.data.promotionCode", is("프로모션코드")))
            .andExpect(jsonPath("$.error", is(nullValue())));

    }

    @Test
    @DisplayName("관리자가 쿠폰을 발행 - 테스트 실패(고정 할인 쿠폰 최소 금액 미달)")
    void createAdminCouponFailureTest() throws Exception {
        //Given
        Long couponAdminId = couponAdminRepository.save(new CouponAdmin("Admin_WOOCOU")).getId();

        CouponCreateRequestDto couponCreateRequestDto = couponConverter
            .convertToCouponCreateRequest(
                TestCouponFactory.builder()
                    .name("기리보이가 쏜다")
                    .amount(999L)
                    .minOrderPrice(10_000L)
                    .discountType(DiscountType.FIXED_AMOUNT)
                    .issuerType(IssuerType.ADMIN)
                    .issuerId(1L)
                    .maxCount(10)
                    .allocatedCount(10)
                    .promotionCode("프로모션코드")
                    .build()
            );

        //When
        ResultActions resultActions = mvc.perform(
                post("/api/v1/coupons/{couponAdminId}", couponAdminId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(couponCreateRequestDto)))
            .andDo(print());

        //Then
        resultActions.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(CouponRestController.class))
            .andExpect(handler().methodName("createAdminCoupon"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.INVALID_INPUT_VALUE.getCode())));
    }

    @Test
    @DisplayName("관리자가 쿠폰을 발행 - 테스트 실패(퍼센티지 할인 쿠폰 최대 퍼센티지 초과)")
    void createAdminCouponFailureTest2() throws Exception {
        //Given
        Long couponAdminId = couponAdminRepository.save(new CouponAdmin("Admin_WOOCOU")).getId();

        CouponCreateRequestDto couponCreateRequestDto = couponConverter
            .convertToCouponCreateRequest(
                TestCouponFactory.builder()
                    .name("기리보이가 쏜다")
                    .amount(101L)
                    .minOrderPrice(10_000L)
                    .discountType(DiscountType.PERCENT_DISCOUNT)
                    .issuerType(IssuerType.ADMIN)
                    .issuerId(1L)
                    .maxCount(10)
                    .allocatedCount(10)
                    .promotionCode("프로모션코드")
                    .build()
            );

        //When
        ResultActions resultActions = mvc.perform(
            post("/api/v1/coupons/{couponAdminId}", couponAdminId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponCreateRequestDto)))
            .andDo(print());

        //Then
        resultActions.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(CouponRestController.class))
            .andExpect(handler().methodName("createAdminCoupon"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.INVALID_INPUT_VALUE.getCode())));
    }

    private ResultActions requestCreateStoreCoupons(long storeId,
        StoreCouponSaveRequestDto requestDto)
        throws Exception {
        return mvc.perform(post("/api/v1/coupons/{storeId}/issuance", storeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
            .andDo(print());
    }
}

