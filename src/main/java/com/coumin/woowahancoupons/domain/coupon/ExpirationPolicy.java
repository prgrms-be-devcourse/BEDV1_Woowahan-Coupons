package com.coumin.woowahancoupons.domain.coupon;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpirationPolicy {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpirationPolicyType expirationPolicyType;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expiredAt;

    @Column(name = "days_from_issuance")
    private Integer daysFromIssuance;

    private ExpirationPolicy(
        ExpirationPolicyType expirationPolicyType, LocalDateTime startAt,
        LocalDateTime expiredAt, Integer daysFromIssuance) {
        this.expirationPolicyType = expirationPolicyType;
        this.startAt = startAt;
        this.expiredAt = expiredAt;
        this.daysFromIssuance = daysFromIssuance;
    }

    public static ExpirationPolicy newByAfterIssueDate(Integer daysFromIssuance) {
        Objects.requireNonNull(daysFromIssuance, "daysFromIssuance must not be null");

        return new ExpirationPolicy(
            ExpirationPolicyType.AFTER_ISSUE_DATE,
            null,
            null,
            daysFromIssuance
        );
    }

    public static ExpirationPolicy newByPeriod(LocalDateTime startAt, LocalDateTime expiredAt) {
        Objects.requireNonNull(startAt, "startAt must not be null");
        Objects.requireNonNull(expiredAt, "expiredAt must not be null");

        return new ExpirationPolicy(
            ExpirationPolicyType.PERIOD,
            startAt,
            expiredAt,
            null
        );
    }
}
