package com.coumin.woowahancoupons.domain.store;

import com.coumin.woowahancoupons.domain.BaseEntity;
import lombok.*;
import javax.persistence.*;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "brands")
@Entity
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Store> stores = new ArrayList<>();
}
