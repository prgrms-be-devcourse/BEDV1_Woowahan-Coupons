package com.coumin.woowahancoupons.coupon.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import com.coumin.woowahancoupons.global.exception.StoreNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleCouponServiceTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private CouponRepository couponRepository;

	@InjectMocks
	private SimpleCouponService couponService;

	@Test
	@DisplayName("매장의 쿠폰 생성 성공")
	void saveAllStoreCouponsSuccessTest() {
		//Given
		long storeId = 1L;
		List<StoreCouponSaveDto> storeCouponSaveDtos = IntStream.range(0, 5)
			.mapToObj(i -> StoreCouponSaveDto.builder()
				.name("name#" + i)
				.amount(1000L * (i + 1))
				.daysAfterIssuance(30)
				.minOrderPrice(1000L * (i + 1))
				.build())
			.collect(Collectors.toList());
		StoreCouponSaveRequestDto requestDto = new StoreCouponSaveRequestDto(storeCouponSaveDtos);

		given(storeRepository.existsById(storeId)).willReturn(true);

		//When
		couponService.saveAllStoreCoupons(storeId, requestDto);

		//Then
		verify(storeRepository, times(1)).existsById(storeId);
		verify(couponRepository, times(1)).saveAll(anyList());
	}

	@Test()
	@DisplayName("매장 id가 유효하지 않으면, 매장의 쿠폰 생성 실패")
	void saveAllStoreCouponsFailTest() {
		//Given
		long storeId = 1L;
		List<StoreCouponSaveDto> storeCouponSaveDtos = IntStream.range(0, 5)
			.mapToObj(i -> StoreCouponSaveDto.builder()
				.name("name#" + i)
				.amount(1000L * (i + 1))
				.daysAfterIssuance(30)
				.minOrderPrice(1000L * (i + 1))
				.build())
			.collect(Collectors.toList());
		StoreCouponSaveRequestDto requestDto = new StoreCouponSaveRequestDto(storeCouponSaveDtos);

		given(storeRepository.existsById(any()))
			.willThrow(new StoreNotFoundException(String.format("store(%d)", storeId)));

		//When, Then
		assertThatThrownBy(() -> couponService.saveAllStoreCoupons(storeId, requestDto))
			.isInstanceOf(StoreNotFoundException.class)
			.hasMessageContaining("is not found");
		verify(storeRepository, only()).existsById(storeId);
	}
}