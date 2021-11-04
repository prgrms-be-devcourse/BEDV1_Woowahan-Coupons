package com.coumin.woowahancoupons.coupon.document;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.ApiDocumentationTest;
import com.coumin.woowahancoupons.coupon.converter.CouponRedemptionConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponIssuanceDto;
import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class CouponRedemptionDocumentationTest extends ApiDocumentationTest {

    @Test
    @DisplayName("고객이 쿠폰 번호를 입력해 쿠폰 등록")
    void registerCouponCodeDocTest() throws Exception {
        //Given
        UUID couponCode = UUID.randomUUID();
        Long customerId = 1L;

        //When
        ResultActions resultActions = mockMvc.perform(
            patch("/api/v1/coupons/{couponCode}/customers/{customerId}/register",
                couponCode,
                customerId)
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("couponCode-register",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("couponCode").description("쿠폰 코드"),
                    parameterWithName("customerId").description("고객 Id")
                )
            ));
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당")
    void allocateCouponDocTest() throws Exception {
        //Given
        Long couponId = 1L;
        Long customerId = 1L;

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/customers/{customerId}/allocate",
                couponId,
                customerId)
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("coupon-allocate-with-issuance",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("couponId").description("쿠폰 Id"),
                    parameterWithName("customerId").description("고객 Id")
                )
            ));
    }

    @Test
    @DisplayName("쿠폰 코드 발행")
    void issueCouponCodesSuccessTest() throws Exception {
        //Given
        Long couponId = 1L;
        CouponIssuanceDto couponIssuanceDto = new CouponIssuanceDto(1);

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/issue", couponId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponIssuanceDto))
        );

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("couponCode-issue",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("couponId").description("쿠폰 Id")
                ),
                requestFields(
                    fieldWithPath("issuanceCount").type(JsonFieldType.NUMBER).description("쿠폰 코드 발행 개수")
                )
            ));
    }

    @Test
    @DisplayName("쿠폰 사용")
    void userCouponRedemptionTest() throws Exception {
        //Given
        Long couponRedemptionId = 1L;

        //When
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/coupons/{couponRedemptionId}/use", couponRedemptionId)
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("coupon-use",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("couponRedemptionId").description("쿠폰발행 Id")
                )
            ));
    }

    @Test
    @DisplayName("고객의 사용가능한 쿠폰리스트 조회")
    void getCustomerCouponRedemptionsTest() throws Exception {
        //Given
        Long customerId = 1L;
        CouponRedemption couponRedemption = CouponRedemption.of(
            TestCouponFactory.builder().minOrderPrice(1000L).build());
        var couponRedemptionConverter = new CouponRedemptionConverter();
        var couponRedemptionResponseDto = couponRedemptionConverter.convertToCouponRedemptionResponseDto(
            couponRedemption);
        given(couponRedemptionService.findCustomerCouponRedemptions(customerId))
            .willReturn(List.of(couponRedemptionResponseDto));

        //When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/coupons/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("customer-availableCoupon-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("customerId").description("고객 Id")
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("쿠폰발행 Id").optional(),
                    fieldWithPath("couponCode").type(JsonFieldType.STRING).description("쿠폰 코드"),
                    fieldWithPath("startAt").type(JsonFieldType.STRING).description("쿠폰 사용 시작 일자"),
                    fieldWithPath("expiredAt").type(JsonFieldType.STRING).description("쿠폰 사용 만료 일자"),
                    fieldWithPath("coupon").type(JsonFieldType.OBJECT).description("쿠폰 상세 정보"),
                    fieldWithPath("coupon.name").type(JsonFieldType.STRING).description("쿠폰 이름"),
                    fieldWithPath("coupon.amount").type(JsonFieldType.NUMBER).description("할인 금액"),
                    fieldWithPath("coupon.minOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                    fieldWithPath("coupon.discountType").type(JsonFieldType.STRING).description("할인 타입"),
                    fieldWithPath("coupon.issuerType").type(JsonFieldType.STRING).description("발행 주체 타입")
                )
            ));
    }
}

