package com.coumin.woowahancoupons.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import org.springframework.data.annotation.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime lastModifiedAt;
}
