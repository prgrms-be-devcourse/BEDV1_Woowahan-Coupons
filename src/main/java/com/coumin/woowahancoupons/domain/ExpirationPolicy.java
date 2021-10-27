package com.coumin.woowahancoupons.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;


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

    @Builder(builderClassName = "ByPeriodTypeBuilder", builderMethodName = "ByPeriodTypeBuilder")
    public ExpirationPolicy(ExpirationPolicyType expirationPolicyType, LocalDateTime startAt,
        LocalDateTime expiredAt) {
        Assert.notNull(expirationPolicyType, "expirationPolicyType must not be null");
        Assert.isTrue(expirationPolicyType == ExpirationPolicyType.PERIOD, "invalid expirationPolicyType");
        Assert.notNull(startAt, "startAt must not be null");
        Assert.notNull(expiredAt, "expiredAt must not be null");

        this.expirationPolicyType = expirationPolicyType;
        this.startAt = startAt;
        this.expiredAt = expiredAt;
    }

    @Builder(builderClassName = "ByAfterIssueDateTypeBuilder", builderMethodName = "ByAfterIssueDateTypeBuilder")
    public ExpirationPolicy(
        ExpirationPolicyType expirationPolicyType,
        Integer daysFromIssuance) {
        Assert.notNull(expirationPolicyType, "expirationPolicyType must not be null");
        Assert.isTrue(expirationPolicyType == ExpirationPolicyType.AFTER_ISSUE_DATE, "invalid expirationPolicyType");
        Assert.notNull(daysFromIssuance, "daysFromIssuance must not be null");

        this.expirationPolicyType = expirationPolicyType;
        this.daysFromIssuance = daysFromIssuance;
    }
}
