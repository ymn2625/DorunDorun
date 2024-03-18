package com.example.dorundorun.repository;

import com.example.dorundorun.entity.CrewMemberEntity;
import com.example.dorundorun.entity.CrewRunningEntity;
import com.example.dorundorun.entity.CrewRunningMemberEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CrewRunningMemberRepository extends JpaRepository<CrewRunningMemberEntity,Long> {

    @Query(value = "SELECT COUNT(*) FROM CrewRunningMemberEntity c WHERE c.crewRunningEntity.crewRunningId = :crewRunningId")
    int countCrewRunningMember(Long crewRunningId);

    Optional<CrewRunningMemberEntity> findByCrewRunningEntityAndMemberEntity(CrewRunningEntity crewRunningEntity, MemberEntity memberEntity);

    List<CrewRunningMemberEntity> findAllByCrewRunningEntityOrderByCrewRunningMemberId(CrewRunningEntity crewRunningEntity);
}
