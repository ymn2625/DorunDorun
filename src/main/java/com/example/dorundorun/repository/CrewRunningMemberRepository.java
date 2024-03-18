package com.example.dorundorun.repository;

import com.example.dorundorun.entity.CrewMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRunningMemberRepository extends JpaRepository<CrewMemberEntity,Long> {
}
