package com.coumin.woowahancoupons.domain.coupon;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.time.LocalDateTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExpirationPolicyTest {

    @Test
    @DisplayName("지정된 기간과 시간을 쿠폰의 유효기간으로 하는 만료 정책 객체 생성 성공")
    void byPeriodTypeBuilderSuccessTest() {
        //Given
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        ExpirationPolicyType expirationPolicyType = ExpirationPolicyType.PERIOD;

        //When
        ExpirationPolicy expirationPolicy = ExpirationPolicy.newByPeriod(startAt, expiredAt);

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(expirationPolicy.getExpirationPolicyType())
                    .isEqualTo(expirationPolicyType);
                softAssertions.assertThat(expirationPolicy.getStartAt()).isEqualTo(startAt);
                softAssertions.assertThat(expirationPolicy.getExpiredAt()).isEqualTo(expiredAt);
            }
        );
    }

    @Test
    @DisplayName("조건에 맞지 않는 경우, 지정된 기간과 시간을 쿠폰의 유효기간으로 하는 만료 정책 객체 생성 실패가 정상")
    void byPeriodTypeBuilderFailTest() {
        //Given
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        //When, Then
        assertThatNullPointerException().isThrownBy(() -> {
            ExpirationPolicy.newByPeriod(null, expiredAt);
        }).withMessage("startAt must not be null");

        assertThatNullPointerException().isThrownBy(() -> {
            ExpirationPolicy.newByPeriod(startAt, null);
        }).withMessage("expiredAt must not be null");
    }

    @Test
    @DisplayName("실제 쿠폰 발급일시를 기준으로 지정된 기간을 쿠폰의 유효기간으로 하는 만료 정책 객체 생성 성공")
    void byAfterIssueDateTypeBuilderSuccessTest() {
        //Given
        ExpirationPolicyType expirationPolicyType = ExpirationPolicyType.AFTER_ISSUE_DATE;
        int days = 7;

        //When
        ExpirationPolicy expirationPolicy = ExpirationPolicy.newByAfterIssueDate(days);

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(expirationPolicy.getExpirationPolicyType())
                    .isEqualTo(expirationPolicyType);
                softAssertions.assertThat(expirationPolicy.getDaysFromIssuance()).isEqualTo(days);
            }
        );
    }

    @Test
    @DisplayName("조건에 맞지 않는 경우, 실제 쿠폰 발급일시를 기준으로 지정된 기간을 쿠폰의 유효기간으로 하는 만료 정책 객체 생성 실패")
    void byAfterIssueDateTypeBuilderFailTest() {
        assertThatNullPointerException().isThrownBy(() -> {
            ExpirationPolicy.newByAfterIssueDate(null);
        }).withMessage("daysFromIssuance must not be null");
    }
}
