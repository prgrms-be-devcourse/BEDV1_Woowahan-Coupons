package com.coumin.woowahancoupons.coupon.document;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.ApiDocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;


class CommonDocumentationTest extends ApiDocumentationTest {

    @Test
    void commonDocs() throws Exception {

        //When
        ResultActions result = mockMvc.perform(
            post("/api/v1/coupons/{couponId}/customers/{customerId}/allocate", 1, 1)
                .accept(MediaType.APPLICATION_JSON)
        );

        //Then
        result.andExpect(status().isOk())
            .andDo(document("common",
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                    subsectionWithPath("data").type(JsonFieldType.OBJECT)
                        .description("요청 결과 데이터 (null if success is false)").optional(),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보 (null if success is true)")
                        .optional(),
                    fieldWithPath("error.code").type(JsonFieldType.STRING).description("에러 코드"),
                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("에러 메시지")
                )
            ));
    }
}

