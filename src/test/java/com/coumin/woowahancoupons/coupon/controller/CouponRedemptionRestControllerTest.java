package com.coumin.woowahancoupons.coupon.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.coupon.dto.CouponCheckRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponIssuanceDto;
import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.coupon.service.CouponRedemptionService;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    private EntityManager entityManager;

    @Autowired
    private CouponRedemptionService couponRedemptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("고객이 쿠폰 번호를 입력해 쿠폰 등록 - 성공 테스트")
    void registerCouponCodeSuccessTest() throws Exception {
        //Given
        Customer givenCustomer = givenCustomer();
        CouponRedemption givenCouponRedemption = givenCouponRedemptionWithoutCustomer();

        //When
        ResultActions result = mockMvc.perform(
            patch("/api/v1/coupons/{couponCode}/customers/{customerId}/register",
                givenCouponRedemption.getCouponCode(),
                givenCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("registerCouponCode"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("고객이 쿠폰 번호를 입력해 쿠폰 등록 - 실패 테스트 (잘못된 쿠폰 코드)")
    void registerCouponCodeFailureTest() throws Exception {
        //Given
        UUID invalidCouponCode = UUID.randomUUID();
        Customer givenCustomer = givenCustomer();

        //When
        ResultActions result = mockMvc.perform(
            patch("/api/v1/coupons/{couponCode}/customers/{customerId}/register",
                invalidCouponCode,
                givenCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("registerCouponCode"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getCode())))
            .andExpect(jsonPath("$.error.message", containsString(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getMessage())));
    }

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 등록 - 실패 테스트 (이미 등록된 쿠폰)")
    void registerCouponCodeFailureTest2() throws Exception {
        //Given
        CouponRedemption givenCouponRedemption = givenCouponRedemptionWithCustomer();

        //When
        ResultActions result = mockMvc.perform(
            patch("/api/v1/coupons/{couponCode}/customers/{customerId}/register",
                givenCouponRedemption.getCouponCode(),
                givenCouponRedemption.getCustomer().getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("registerCouponCode"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_REDEMPTION_ALREADY_ALLOCATE.getCode())))
            .andExpect(jsonPath("$.error.message", containsString(ErrorCode.COUPON_REDEMPTION_ALREADY_ALLOCATE.getMessage())));
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당 - 성공 테스트")
    void allocateCouponSuccessTest() throws Exception {
        //Given
        Coupon givenCoupon = givenCoupon();
        Customer givenCustomer = givenCustomer();

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/customers/{customerId}/allocate",
                givenCoupon.getId(),
                givenCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("allocateCoupon"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당 - 실패 테스트 (잘못된 쿠폰 Id)")
    void allocateCouponFailureTest() throws Exception {
        //Given
        Long invalidCouponId = 0L;
        Customer givenCustomer = givenCustomer();

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/customers/{customerId}/allocate",
                invalidCouponId,
                givenCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("allocateCoupon"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_NOT_FOUND.getCode())))
            .andExpect(jsonPath("$.error.message", containsString(ErrorCode.COUPON_NOT_FOUND.getMessage())));
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당 - 실패 테스트 (잘못된 고객 Id)")
    void allocateCouponFailureTest2() throws Exception {
        //Given
        Coupon givenCoupon = givenCoupon();
        Long invalidCustomerId = 0L;

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/customers/{customerId}/allocate",
                givenCoupon.getId(),
                invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("allocateCoupon"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.CUSTOMER_NOT_FOUND.getCode())))
            .andExpect(jsonPath("$.error.message", containsString(ErrorCode.CUSTOMER_NOT_FOUND.getMessage())));
    }

    @Test
    @DisplayName("쿠폰 코드 발행 - 성공 테스트")
    void issueCouponCodesSuccessTest() throws Exception {
        //Given
        Coupon coupon = TestCouponFactory.builder().maxCount(10).build();
        CouponIssuanceDto couponIssuanceDto = new CouponIssuanceDto(3);
        entityManager.persist(coupon);

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/issue",
                coupon.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponIssuanceDto))
        );

        //Then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("issueCouponCodes"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("쿠폰 코드 발행 - 실패 테스트 (쿠폰 발행 상한을 넘는 발행 수로 쿠폰 코드 발행을 시도)")
    void issueCouponCodesFailureTest() throws Exception {
        //Given
        Coupon coupon = TestCouponFactory.builder().maxCount(10).build();
        CouponIssuanceDto couponIssuanceDto = new CouponIssuanceDto(11);
        entityManager.persist(coupon);

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/issue",
                coupon.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponIssuanceDto))
        );

        //Then
        result.andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("issueCouponCodes"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_MAX_COUNT_OVER.getCode())));
    }

    @DisplayName("쿠폰 코드 발행 - 실패 테스트 (발행 개수가 0 이하)")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void issueCouponCodesFailureTest2(int issuanceCount) throws Exception {
        //Given
        Coupon coupon = TestCouponFactory.builder().maxCount(10).build();
        CouponIssuanceDto couponIssuanceDto = new CouponIssuanceDto(issuanceCount);
        entityManager.persist(coupon);

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/issue",
                coupon.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponIssuanceDto))
        );

        //Then
        result.andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("issueCouponCodes"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.INVALID_INPUT_VALUE.getCode())));
    }

    @Test
    @DisplayName("고객의 사용가능한 쿠폰리스트 조회")
    void getCustomerCouponRedemptionsTest() throws Exception {

        //Given
        Coupon givenCoupon = givenCoupon();
        Customer givenCustomer = givenCustomer();
        Long customerId = givenCustomer.getId();

        IntStream.range(0, 2)
            .mapToObj(i -> CouponRedemption.of(givenCoupon, givenCustomer))
            .forEach(couponRedemption -> entityManager.persist(couponRedemption));
        IntStream.range(0, 2)
            .mapToObj(i -> CouponRedemption.of(givenCoupon, givenCustomer))
            .forEach(couponRedemption -> {
                couponRedemption.use();
                entityManager.persist(couponRedemption);
            });
        Coupon expiredCoupon = TestCouponFactory.builder()
            .expirationPolicy(ExpirationPolicy.newByPeriod(
                LocalDateTime.now().minusYears(10),
                LocalDateTime.now().minusYears(5)
            )).build();
        entityManager.persist(expiredCoupon);
        IntStream.range(0, 2)
            .mapToObj(i -> CouponRedemption.of(expiredCoupon, givenCustomer))
            .forEach(couponRedemption -> entityManager.persist(couponRedemption));

        //When
        ResultActions result = mockMvc.perform(get("/api/v1/coupons/{customerId}", customerId)
            .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("getCustomerCouponRedemptions"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(2)))
            .andExpect(jsonPath("$.data[0].id", is(notNullValue())))
            .andExpect(jsonPath("$.data[0].couponCode", is(notNullValue())))
            .andExpect(jsonPath("$.data[0].startAt", is(notNullValue())))
            .andExpect(jsonPath("$.data[0].expiredAt", is(notNullValue())))
            .andExpect(jsonPath("$.data[0].coupon", is(notNullValue())))
            .andExpect(jsonPath("$.data[0].coupon.name", is("test coupon")))
            .andExpect(jsonPath("$.data[0].coupon.amount", is(1000)))
            .andExpect(jsonPath("$.data[0].coupon.minOrderPrice", is(nullValue())))
            .andExpect(jsonPath("$.data[0].coupon.discountType", is("FIXED_AMOUNT")))
            .andExpect(jsonPath("$.data[0].coupon.issuerType", is("ADMIN")))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("쿠폰 사용 가능 유무 확인 - 성공 테스트")
    void checkCouponRedemptionForUseSuccessTest() throws Exception {
        //Given
        Long storeId = 1L;
        IssuerType issuerType = IssuerType.STORE;
        Coupon coupon = TestCouponFactory.builder()
            .issuerId(storeId)
            .issuerType(issuerType)
            .build();
        CouponRedemption couponRedemption = givenCouponRedemptionWithCustomer(coupon);
        CouponCheckRequestDto couponCheckRequestDto = new CouponCheckRequestDto(storeId, 10000L);

        //When
        ResultActions result = requestCheckCouponRedemptionForUse(
            couponRedemption.getId(),
            couponCheckRequestDto
        );

        //Then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("checkCouponRedemptionForUse"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("쿠폰 사용 가능 유무 확인 테스트 - 실패 테스트 (최소 주문 금액 미달)")
    void checkCouponRedemptionForUseFailureTest() throws Exception {
        //Given
        Long storeId = 1L;
        IssuerType issuerType = IssuerType.STORE;
        Long minOrderPrice = 10000L;
        Coupon coupon = TestCouponFactory.builder()
            .issuerId(storeId)
            .issuerType(issuerType)
            .minOrderPrice(minOrderPrice)
            .build();
        CouponRedemption couponRedemption = givenCouponRedemptionWithCustomer(coupon);
        CouponCheckRequestDto couponCheckRequestDto = new CouponCheckRequestDto(storeId, 9999L);

        //When
        ResultActions result = requestCheckCouponRedemptionForUse(
            couponRedemption.getId(),
            couponCheckRequestDto
        );

        //Then
        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("checkCouponRedemptionForUse"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_SATISFY.getCode())));
    }

    @Test
    @DisplayName("쿠폰 사용 가능 유무 확인 테스트 - 실패 테스트 (발급 아이디 다름)")
    void checkCouponRedemptionForUseFailureTest2() throws Exception {
        //Given
        Long storeId = 1L;
        IssuerType issuerType = IssuerType.STORE;
        Long minOrderPrice = 10000L;
        Coupon coupon = TestCouponFactory.builder()
            .issuerId(storeId)
            .issuerType(issuerType)
            .minOrderPrice(minOrderPrice)
            .build();
        CouponRedemption couponRedemption = givenCouponRedemptionWithCustomer(coupon);
        CouponCheckRequestDto couponCheckRequestDto = new CouponCheckRequestDto(2L, 9999L);

        //When
        ResultActions result = requestCheckCouponRedemptionForUse(
            couponRedemption.getId(),
            couponCheckRequestDto
        );

        //Then
        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("checkCouponRedemptionForUse"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_ISSUER_ID_NOT_MATCH.getCode())));
    }

    @Test
    @DisplayName("쿠폰 사용 - 성공 테스트")
    void userCouponRedemptionTest() throws Exception {
        //Given
        CouponRedemption couponRedemption = givenCouponRedemptionWithCustomer();

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponRedemptionId}/use",
                couponRedemption.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("userCouponRedemption"))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("쿠폰 사용 - 실패 테스트 (이미 사용된 쿠폰)")
    void userCouponRedemptionFailureTest() throws Exception {
        //Given
        CouponRedemption couponRedemption = givenCouponRedemptionWithCustomer();
        couponRedemption.use();

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponRedemptionId}/use",
                couponRedemption.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("userCouponRedemption"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_REDEMPTION_ALREADY_USE.getCode())));
    }

    @Test
    @DisplayName("쿠폰 사용 - 실패 테스트 (쿠폰의 사용 기한 만료)")
    void userCouponRedemptionFailureTest2() throws Exception {
        //Given
        Coupon coupon = TestCouponFactory.builder()
            .expirationPolicy(ExpirationPolicy.newByPeriod(
                LocalDateTime.of(2000, 12, 31, 23, 59, 59),
                LocalDateTime.of(2001, 12, 31, 23, 59, 59)
            )).build();
        CouponRedemption couponRedemption = givenCouponRedemptionWithCustomer(coupon);

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponRedemptionId}/use",
                couponRedemption.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(CouponRedemptionRestController.class))
            .andExpect(handler().methodName("userCouponRedemption"))
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.COUPON_REDEMPTION_EXPIRE.getCode())));
    }

    private Coupon givenCoupon() {
        Coupon coupon = TestCouponFactory.builder().build();
        entityManager.persist(coupon);
        return coupon;
    }

    private Customer givenCustomer() {
        Customer customer = new Customer("test@gmail.com");
        entityManager.persist(customer);
        return customer;
    }

    private CouponRedemption givenCouponRedemptionWithCustomer() {
        Coupon coupon = TestCouponFactory.builder().build();
        Customer customer = new Customer("test@gmail.com");
        CouponRedemption couponRedemption = CouponRedemption.of(coupon, customer);
        entityManager.persist(coupon);
        entityManager.persist(customer);
        entityManager.persist(couponRedemption);
        return couponRedemption;
    }

    private CouponRedemption givenCouponRedemptionWithCustomer(Coupon coupon) {
        if(coupon == null) {
            return givenCouponRedemptionWithCustomer();
        }

        Customer customer = new Customer("test@gmail.com");
        CouponRedemption couponRedemption = CouponRedemption.of(coupon, customer);
        entityManager.persist(coupon);
        entityManager.persist(customer);
        entityManager.persist(couponRedemption);
        return couponRedemption;
    }

    private CouponRedemption givenCouponRedemptionWithoutCustomer() {
        Coupon coupon = TestCouponFactory.builder().build();
        CouponRedemption couponRedemption = CouponRedemption.of(coupon);
        entityManager.persist(coupon);
        entityManager.persist(couponRedemption);
        return couponRedemption;
    }

    private ResultActions requestCheckCouponRedemptionForUse(
        Long couponRedemptionId,
        CouponCheckRequestDto couponCheckRequestDto
    ) throws Exception {

        return mockMvc.perform(
            post("/api/v1/coupons/{couponRedemptionId}/check", couponRedemptionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(couponCheckRequestDto)));
    }
}
