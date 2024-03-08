package com.example.dorundorun.repository;

import com.example.dorundorun.dto.BoardDTO;
import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.entity.BoardEntity;
import com.example.dorundorun.entity.BoardLikeEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity, Long> {

     @Query(value = "SELECT count(*) FROM BoardLikeEntity b WHERE b.boardEntity.boardId = :boardId AND b.boardLike=1")
     int countLike(@Param("boardId") Long boardId);

     @Query(value = "SELECT COUNT(*) FROM BoardLikeEntity b WHERE b.boardHate = 1 AND b.boardEntity.boardId = :boardId")
     int countHate(@Param("boardId") Long boardId);

     BoardLikeEntity findByBoardEntityAndMemberEntity(BoardEntity boardEntity, MemberEntity memberEntity);

}
