package com.coumin.woowahancoupons.coupon.document;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.ApiDocumentationTest;
import com.coumin.woowahancoupons.coupon.converter.CouponConverter;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class CouponDocumentationTest extends ApiDocumentationTest {

    @Test
    @DisplayName("매장의 쿠폰 생성")
    void createStoreCouponsDocTest() throws Exception {
        //Given
        long storeId = 1;
        var testStoreCouponSaveDto = StoreCouponSaveDto.builder()
            .name("string")
            .amount(1000L)
            .daysAfterIssuance(30)
            .minOrderPrice(1000L)
            .build();
        var storeCouponSaveRequestDto = new StoreCouponSaveRequestDto(List.of(testStoreCouponSaveDto));

        //When
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/coupons/{storeId}/issuance", storeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storeCouponSaveRequestDto)))
            .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("store-coupon-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("storeId").description("매장 Id")
                ),
                requestFields(
                    fieldWithPath("storeCouponSaves").type(JsonFieldType.ARRAY).description("쿠폰 저장 리스트"),
                    fieldWithPath("storeCouponSaves[].name").type(JsonFieldType.STRING).description("쿠폰 이름"),
                    fieldWithPath("storeCouponSaves[].amount").type(JsonFieldType.NUMBER).description("할인 금액"),
                    fieldWithPath("storeCouponSaves[].daysAfterIssuance").type(JsonFieldType.NUMBER)
                        .description("유효 일 수(발급 일자 기준)"),
                    fieldWithPath("storeCouponSaves[].minOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액")
                )
            ));
    }

    @Test
    @DisplayName("관리자가 쿠폰을 발행 할 수 있다.")
    void createAdminCouponTest() throws Exception {
        //Given
        Long couponAdminId = 1L;
        Coupon coupon = TestCouponFactory.builder()
            .minOrderPrice(1000L)
            .maxCount(500)
            .maxCountPerCustomer(3)
            .promotionCode("프로모션코드")
            .build();
        var couponConverter = new CouponConverter();
        var couponCreateRequestDto = couponConverter.convertToCouponCreateRequest(coupon);

        //When
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/coupons/{couponAdminId}", couponAdminId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(couponCreateRequestDto)))
            .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("admin-coupon-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("couponAdminId").description("관리자 Id")
                ),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("쿠폰 이름"),
                    fieldWithPath("amount").type(JsonFieldType.NUMBER).description("할인 금액"),
                    fieldWithPath("minOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                    fieldWithPath("discountType").type(JsonFieldType.STRING).description("할인 타입"),
                    fieldWithPath("issuerType").type(JsonFieldType.STRING).description("발행 주체 타입"),
                    fieldWithPath("issuerId").type(JsonFieldType.NUMBER).description("발행 주체 Id"),
                    fieldWithPath("allocatedCount").type(JsonFieldType.NUMBER).description("할당 수 (deprecated)"),
                    fieldWithPath("maxCount").type(JsonFieldType.NUMBER).description("최대 발행 수"),
                    fieldWithPath("maxCountPerCustomer").type(JsonFieldType.NUMBER).description("고객 당 최대 발행 수"),
                    fieldWithPath("promotionCode").type(JsonFieldType.STRING).description("프로모션 코드"),
                    fieldWithPath("expirationPolicyType").type(JsonFieldType.STRING).description("유효기간 정책 타입"),
                    fieldWithPath("startAt").type(JsonFieldType.STRING).description("쿠폰 사용 시작 일자"),
                    fieldWithPath("expiredAt").type(JsonFieldType.STRING).description("쿠폰 사용 만료 일자"),
                    fieldWithPath("daysFromIssuance").type(JsonFieldType.NUMBER).description("유효 일 수(발급 일자 기준)")
                        .optional()
                )
            ));

    }
}

