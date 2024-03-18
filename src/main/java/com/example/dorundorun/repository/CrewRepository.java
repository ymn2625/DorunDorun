package com.example.dorundorun.repository;

import com.example.dorundorun.entity.CrewEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewRepository extends JpaRepository<CrewEntity, Long> {
    CrewEntity findByCrewName(String crewName);

}
