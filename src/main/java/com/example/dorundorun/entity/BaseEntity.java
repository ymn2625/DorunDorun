package com.example.dorundorun.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp
    @Column(updatable = false)  //수정시에 관여 x
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(insertable = false) //입력시에 관여 x
    private LocalDateTime updatedTime;
}
