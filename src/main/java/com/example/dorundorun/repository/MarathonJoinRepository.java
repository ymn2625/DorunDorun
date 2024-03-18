package com.example.dorundorun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.dorundorun.entity.MarathonJoinEntity;

@Repository
public interface MarathonJoinRepository extends JpaRepository<MarathonJoinEntity, Long> {

    MarathonJoinEntity findByMemberEntityId(Long memberId);
}
