package com.example.dorundorun.repository;

import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.entity.RunningRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningRecordRepository extends JpaRepository<RunningRecordEntity, Long> {
    List<RunningRecordEntity> findAllByMemberEntity(MemberEntity memberEntity);

}
