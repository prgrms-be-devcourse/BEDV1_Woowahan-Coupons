package com.coumin.woowahancoupons.domain.coupon;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpirationPeriod {

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiredAt;

    private ExpirationPeriod(LocalDateTime startAt, LocalDateTime expiredAt) {
        this.startAt = startAt;
        this.expiredAt = expiredAt;
    }

    public static ExpirationPeriod from(ExpirationPolicy expirationPolicy) {
        if (expirationPolicy.getExpirationPolicyType() == ExpirationPolicyType.PERIOD) {
            return new ExpirationPeriod(
                expirationPolicy.getStartAt(),
                expirationPolicy.getExpiredAt()
            );
        } else {
            LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
            return new ExpirationPeriod(
            	now,
				now.plusDays(expirationPolicy.getDaysFromIssuance())
			);
        }
    }
}
