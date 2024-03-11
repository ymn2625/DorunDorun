package com.example.dorundorun.repository;

import com.example.dorundorun.dto.BoardDTO;
import com.example.dorundorun.entity.BoardEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.boardId=:id")
    void updateHits(@Param("id") Long id);

    Page<BoardEntity> findByBoardTitleContaining(String keyword, Pageable pageable);

    Page<BoardEntity> findByMemberEntityIn(List<MemberEntity> byMemberNicknameContaining, Pageable pageable);
}
