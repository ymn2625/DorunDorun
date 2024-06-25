package com.example.dorundorun.repository;

import com.example.dorundorun.entity.board.BoardCommentEntity;
import com.example.dorundorun.entity.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardCommentEntity, Long> {

    List<BoardCommentEntity> findAllByBoardEntityOrderByBoardCommentIdDesc(BoardEntity boardEntity);
}
