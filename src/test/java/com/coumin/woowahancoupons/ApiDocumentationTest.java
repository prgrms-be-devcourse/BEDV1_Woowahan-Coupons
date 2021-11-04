package com.coumin.woowahancoupons;

import com.coumin.woowahancoupons.coupon.controller.CouponRedemptionRestController;
import com.coumin.woowahancoupons.coupon.controller.CouponRestController;
import com.coumin.woowahancoupons.coupon.service.CouponRedemptionService;
import com.coumin.woowahancoupons.coupon.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(controllers = {
    CouponRestController.class,
    CouponRedemptionRestController.class
})
public abstract class ApiDocumentationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected CouponService couponService;

    @MockBean
    protected CouponRedemptionService couponRedemptionService;

}
