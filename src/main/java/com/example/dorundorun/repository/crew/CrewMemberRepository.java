package com.example.dorundorun.repository;

import com.example.dorundorun.dto.CrewMemberDTO;
import com.example.dorundorun.entity.CrewEntity;
import com.example.dorundorun.entity.CrewMemberEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMemberEntity, Long> {

    @Query(value = "SELECT COUNT(*) FROM CrewMemberEntity c WHERE c.crewEntity.crewId = :crewId")
    int countCrewMember(@Param("crewId") Long crewId);

    Optional<CrewMemberEntity> findByCrewEntityAndMemberEntity(CrewEntity crewEntity, MemberEntity memberEntity);

    List<CrewMemberEntity> findAllByCrewEntityOrderByRole(CrewEntity crewEntity);

    @Query(value = "SELECT c FROM CrewMemberEntity c WHERE c.role = 'CREW_ADMIN' AND c.crewEntity.crewId = :crewId")
    CrewMemberEntity findAdminByCrewId(@Param("crewId") Long crewId);

    List<CrewMemberEntity> findAllByMemberEntity(MemberEntity memberEntity);
}
