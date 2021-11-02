package com.coumin.woowahancoupons.domain.coupon;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpirationPeriod {

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiredAt;

    public ExpirationPeriod(LocalDateTime startAt, LocalDateTime expiredAt) {
        this.startAt = startAt;
        this.expiredAt = expiredAt;
    }

    public boolean isExpiration() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
