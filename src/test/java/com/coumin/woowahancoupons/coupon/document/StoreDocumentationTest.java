package com.coumin.woowahancoupons.coupon.document;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponResponseDto;
import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class StoreDocumentationTest extends ApiDocumentationTest {

    @Test
    @DisplayName("매장의 쿠폰 리스트 조회")
    void findStoreCouponsDocTest() throws Exception {
        //Given
        long storeId = 1L;
        Coupon coupon = spy(TestCouponFactory.builder()
            .expirationPolicy(ExpirationPolicy.newByAfterIssueDate(30))
            .minOrderPrice(10_000L)
            .build());
        given(coupon.getId()).willReturn(1L);
        var StoreCouponResponseDtoList = List.of(new StoreCouponResponseDto(coupon));
        given(storeService.findStoreCoupons(storeId))
            .willReturn(StoreCouponResponseDtoList);

        //When
        ResultActions resultActions = mockMvc.perform(
            get("/api/v1/stores/{storeId}/coupons", storeId)
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        resultActions.andExpect(status().isOk())
            .andDo(document("store-coupon-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("storeId").description("매장 Id")
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("쿠폰 Id").optional(),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("쿠폰 이름"),
                    fieldWithPath("amount").type(JsonFieldType.NUMBER).description("할인 금액"),
                    fieldWithPath("minOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                    fieldWithPath("daysAfterIssuance").type(JsonFieldType.NUMBER).description("유효 일 수(발급 일자 기준)")
                )
            ));
    }
}
