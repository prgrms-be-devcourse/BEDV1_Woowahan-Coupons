package com.coumin.woowahancoupons.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpirationPeriod {

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expiredAt;

}
