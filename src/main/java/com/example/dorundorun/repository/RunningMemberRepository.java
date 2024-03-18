package com.example.dorundorun.repository;

import com.example.dorundorun.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RunningMemberRepository extends JpaRepository<RunningMemberEntity, Long> {

    // runningMember 전체인원
    @Query(value = "SELECT COUNT(*) FROM RunningMemberEntity r WHERE r.runningEntity.runningId = :runningId")
    int countRunningMember(@Param("runningId") Long runningId);

    Optional<RunningMemberEntity> findByRunningEntityAndMemberEntity(RunningEntity runningEntity, MemberEntity memberEntity);

    // 러닝 번개 모임장(글쓴이) 권한 문제...
    List<RunningMemberEntity> findAllByRunningEntityOrderByRunningMemberId(RunningEntity runningEntity);
    @Query(value = "SELECT r FROM RunningMemberEntity r WHERE r.runningMemberId = 1 AND r.runningEntity.runningId = :runningId")
    RunningMemberEntity findAdminByRunningId(@Param("runningId") Long runningId);


}
