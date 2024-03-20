package com.example.dorundorun.repository;

import com.example.dorundorun.entity.RunningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningRepository extends JpaRepository<RunningEntity,Long> {

    RunningEntity findByRunningName(String runningName);

    List<RunningEntity> findAllByOrderByRunningDateAsc();
}
