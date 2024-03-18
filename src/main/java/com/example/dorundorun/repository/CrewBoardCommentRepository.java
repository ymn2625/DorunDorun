package com.example.dorundorun.repository;

import com.example.dorundorun.entity.BoardCommentEntity;
import com.example.dorundorun.entity.BoardEntity;
import com.example.dorundorun.entity.CrewBoardCommentEntity;
import com.example.dorundorun.entity.CrewBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewBoardCommentRepository extends JpaRepository<CrewBoardCommentEntity,Long> {
    List<CrewBoardCommentEntity> findAllByCrewBoardEntityOrderByCrewBoardCommentIdDesc(CrewBoardEntity crewBoardEntity);

}
