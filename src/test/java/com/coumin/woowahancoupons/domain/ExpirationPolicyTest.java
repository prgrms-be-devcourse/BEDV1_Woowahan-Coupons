package com.coumin.woowahancoupons.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExpirationPolicyTest {

    @Test
    @DisplayName("지정된 기간과 시간을 쿠폰의 유효기간으로 하는 만료 정책 객체 생성 성공")
    void byPeriodTypeBuilderSuccessTest() {
        //Given
        var startAt = LocalDateTime.now();
        var expiredAt = LocalDateTime.now().plusDays(7);
        var expirationPolicyType = ExpirationPolicyType.PERIOD;

        //When
        ExpirationPolicy expirationPolicy = ExpirationPolicy.ByPeriodTypeBuilder()
            .expirationPolicyType(expirationPolicyType)
            .startAt(startAt)
            .expiredAt(expiredAt)
            .build();

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
        var startAt = LocalDateTime.now();
        var expiredAt = LocalDateTime.now().plusDays(7);

        //When, Then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            ExpirationPolicy.ByPeriodTypeBuilder()
                .expiredAt(startAt)
                .build();
        }).withMessage("expirationPolicyType must not be null");

        assertThatIllegalArgumentException().isThrownBy(() -> {
            ExpirationPolicy.ByPeriodTypeBuilder()
                .expirationPolicyType(ExpirationPolicyType.AFTER_ISSUE_DATE)
                .startAt(startAt)
                .expiredAt(expiredAt)
                .build();
        }).withMessage("invalid expirationPolicyType");

        assertThatIllegalArgumentException().isThrownBy(() -> {
            ExpirationPolicy.ByPeriodTypeBuilder()
                .expirationPolicyType(ExpirationPolicyType.PERIOD)
                .expiredAt(expiredAt)
                .build();
        }).withMessage("startAt must not be null");

        assertThatIllegalArgumentException().isThrownBy(() -> {
            ExpirationPolicy.ByPeriodTypeBuilder()
                .expirationPolicyType(ExpirationPolicyType.PERIOD)
                .startAt(startAt)
                .build();
        }).withMessage("expiredAt must not be null");
    }

    @Test
    @DisplayName("실제 쿠폰 발급일시를 기준으로 지정된 기간을 쿠폰의 유효기간으로 하는 만료 정책 객체 생성 성공")
    void byAfterIssueDateTypeBuilderSuccessTest() {
        //Given
        var expirationPolicyType = ExpirationPolicyType.AFTER_ISSUE_DATE;
        var days = 7;

        //When
        var expirationPolicy = ExpirationPolicy.ByAfterIssueDateTypeBuilder()
            .expirationPolicyType(expirationPolicyType)
            .daysFromIssuance(days)
            .build();

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
        //Given
        var days = Integer.valueOf(7);

        //When, Then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            ExpirationPolicy.ByAfterIssueDateTypeBuilder()
                .daysFromIssuance(days)
                .build();
        }).withMessage("expirationPolicyType must not be null");

        assertThatIllegalArgumentException().isThrownBy(() -> {
            ExpirationPolicy.ByAfterIssueDateTypeBuilder()
                .expirationPolicyType(ExpirationPolicyType.PERIOD)
                .daysFromIssuance(days)
                .build();
        }).withMessage("invalid expirationPolicyType");

        assertThatIllegalArgumentException().isThrownBy(() -> {
            ExpirationPolicy.ByAfterIssueDateTypeBuilder()
                .expirationPolicyType(ExpirationPolicyType.AFTER_ISSUE_DATE)
                .build();
        }).withMessage("daysFromIssuance must not be null");
    }
}
