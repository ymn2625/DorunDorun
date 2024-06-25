package com.example.dorundorun.repository;

import com.example.dorundorun.entity.CrewBoardEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewBoardRepository extends JpaRepository<CrewBoardEntity,Long> {
    Page<CrewBoardEntity> findByCrewBoardTitleContaining(String keyword, PageRequest crewBoardId);

    Page<CrewBoardEntity> findByMemberEntityIn(List<MemberEntity> byMemberNicknameContaining, PageRequest crewBoardId);

    Page<CrewBoardEntity> findByCrewBoardCategory(String category, PageRequest crewBoardId);

    Page<CrewBoardEntity> findByCrewBoardCategoryAndCrewBoardTitleContaining(String category, String keyword, PageRequest crewBoardId);

    Page<CrewBoardEntity> findByCrewBoardCategoryAndMemberEntityIn(String category, List<MemberEntity> byMemberNicknameContaining, PageRequest crewBoardId);

    @Modifying
    @Query(value = "update CrewBoardEntity c set c.crewBoardHits=c.crewBoardHits+1 where c.crewBoardId=:crewBoardId")
    void updateHits(@Param("crewBoardId") Long crewBoardId);
}
