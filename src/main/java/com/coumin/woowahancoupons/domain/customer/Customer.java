package com.coumin.woowahancoupons.domain.customer;


import com.coumin.woowahancoupons.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public Customer(String email) {
        this.email = email;
    }
}
