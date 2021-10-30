package com.coumin.woowahancoupons.coupon.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		List<StoreCouponSaveRequestDto> requestDtoList = IntStream.range(0, 3)
			.mapToObj(i -> StoreCouponSaveRequestDto.builder()
				.name("name#" + i)
				.amount(1000L * (i + 1))
				.daysAfterIssuance(30)
				.minOrderPrice(1000L * (i + 1))
				.build())
			.collect(Collectors.toList());

		//When
		ResultActions resultActions = requestCreateStoreCoupons(storeId, requestDtoList);

		//TODO rest docs
		//Then
		resultActions
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("매장의 쿠폰 생성 실패 - 유효하지 않은 쿠폰 데이터 사이즈")
	void createStoreCouponsInvalidRequestSizeTest() throws Exception {
		// given
		long storeId = storeRepository.save(new Store("testStore#1")).getId();
		List<StoreCouponSaveRequestDto> requestDtoList = IntStream.range(0, 5)
			.mapToObj(i -> StoreCouponSaveRequestDto.builder()
				.name("name#" + i)
				.amount(1000L * (i + 1))
				.daysAfterIssuance(30)
				.minOrderPrice(1000L * (i + 1))
				.build())
			.collect(Collectors.toList());

		//When
		ResultActions resultActions = requestCreateStoreCoupons(storeId, requestDtoList);

		//TODO rest docs
		//Then
		resultActions
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("매장의 쿠폰 생성 실패 - 유효하지 않은 쿠폰 데이터 값")
	void createStoreCouponsInvalidRequestValueTest() throws Exception {
		// given
		long storeId = storeRepository.save(new Store("testStore#1")).getId();
		List<StoreCouponSaveRequestDto> requestDtoList = IntStream.range(0, 2)
			.mapToObj(i -> StoreCouponSaveRequestDto.builder()
				.name("name#" + i)
				.amount(0L)
				.daysAfterIssuance(0)
				.minOrderPrice(0L)
				.build())
			.collect(Collectors.toList());

		//When
		ResultActions resultActions = requestCreateStoreCoupons(storeId, requestDtoList);

		//TODO rest docs
		//Then
		resultActions
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("매장의 쿠폰 생성 실패 - 유효하지 않은 요청 메서드")
	void createStoreCouponsInvalidRequestMethodTest() throws Exception {
		// given
		long storeId = storeRepository.save(new Store("testStore#1")).getId();
		List<StoreCouponSaveRequestDto> requestDtoList = IntStream.range(0, 5)
			.mapToObj(i -> StoreCouponSaveRequestDto.builder()
				.name("name#" + i)
				.amount(1000L * (i + 1))
				.daysAfterIssuance(30)
				.minOrderPrice(1000L * (i + 1))
				.build())
			.collect(Collectors.toList());

		//When
		ResultActions resultActions = mvc.perform(put("/api/v1/coupons/{storeId}/issuance", storeId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDtoList)))
			.andDo(print());

		//TODO rest docs
		//Then
		resultActions
			.andExpect(status().isMethodNotAllowed())
			.andDo(print());
	}

	private ResultActions requestCreateStoreCoupons(long storeId,
		List<StoreCouponSaveRequestDto> requestDto)
		throws Exception {
		return mvc.perform(post("/api/v1/coupons/{storeId}/issuance", storeId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto)))
			.andDo(print());
	}
}

