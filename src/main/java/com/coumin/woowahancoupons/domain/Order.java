package com.coumin.woowahancoupons.domain;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
}
