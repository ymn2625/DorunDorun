package com.example.dorundorun.repository;

import com.example.dorundorun.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CrewBoardLikeRepository extends JpaRepository<CrewBoardLikeEntity,Long> {
    @Query(value = "SELECT count(*) FROM CrewBoardLikeEntity c WHERE c.crewBoardEntity.crewBoardId = :crewBoardId AND c.crewBoardLike=1")
    int countCrewBoardLike(@Param("crewBoardId") Long crewBoardId);

    @Query(value = "SELECT COUNT(*) FROM CrewBoardLikeEntity c WHERE c.crewBoardHate = 1 AND c.crewBoardEntity.crewBoardId = :crewBoardId")
    int countCrewBoardHate(@Param("crewBoardId") Long crewBoardId);

    CrewBoardLikeEntity findByCrewBoardEntityAndMemberEntity(CrewBoardEntity crewBoardEntity, MemberEntity memberEntity);
}
