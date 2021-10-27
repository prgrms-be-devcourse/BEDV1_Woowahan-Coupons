package com.coumin.woowahancoupons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WoowahanCouponsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WoowahanCouponsApplication.class, args);
    }
}
