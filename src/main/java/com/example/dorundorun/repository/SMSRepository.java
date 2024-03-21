package com.example.dorundorun.repository;

import com.example.dorundorun.entity.SMSEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SMSRepository extends JpaRepository<SMSEntity, Long> {
    Optional<SMSEntity> findByTelAndCertificationCode(String tel, String certificationCode);

    void deleteByTelAndCertificationCode(String tel, String certificationCode);

}
