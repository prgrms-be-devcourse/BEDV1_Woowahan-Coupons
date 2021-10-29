package com.coumin.woowahancoupons.coupon.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.domain.store.Store;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
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

	@Test
	@DisplayName("매장의 쿠폰 생성 성공")
	void createStoreCouponsSuccessTest() throws Exception {
		// given
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
	@DisplayName("요청이 유효하지 않으면 매장의 쿠폰 생성 실패")
	void createStoreCouponsFailTest() throws Exception {
		// given
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

	private ResultActions requestCreateStoreCoupons(long storeId,
		StoreCouponSaveRequestDto requestDto)
		throws Exception {
		return mvc.perform(post("/api/v1/coupons/{storeId}/issuance", storeId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto)))
			.andDo(print());
	}
}