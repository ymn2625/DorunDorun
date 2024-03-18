package com.example.dorundorun.repository;

import com.example.dorundorun.entity.CrewRunningEntity;
import com.example.dorundorun.entity.RunningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRunningRepository extends JpaRepository<CrewRunningEntity,Long> {
    CrewRunningEntity findByCrewRunningName(String crewRunningName);
}
