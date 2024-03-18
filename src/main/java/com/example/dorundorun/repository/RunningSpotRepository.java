package com.example.dorundorun.repository;

import com.example.dorundorun.entity.RunningSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RunningSpotRepository extends JpaRepository<RunningSpotEntity, Long> {
    @Query("SELECT rs.spotName FROM RunningSpotEntity rs " +
            "JOIN RunningEntity r on rs.spotId = r.runningSpotEntity.spotId " +
            "WHERE r.runningId = :runningId")
    List<String> findSpotNamesByRunningId(@Param("runningId") Long runningId);

    Optional<RunningSpotEntity> findBySpotId(Long spotId);


}
