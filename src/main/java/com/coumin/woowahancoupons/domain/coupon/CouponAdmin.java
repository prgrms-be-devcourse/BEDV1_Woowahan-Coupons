package com.coumin.woowahancoupons.domain.coupon;

import com.coumin.woowahancoupons.domain.BaseEntity;
import lombok.*;
import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "admin")
@Entity
public class CouponAdmin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    public CouponAdmin(String name) {
        this.name = name;
    }
}
