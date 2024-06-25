package com.example.dorundorun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.dorundorun.entity.MarathonCourseEntity;

import java.util.Optional;

@Repository
public interface MarathonCourseRepository extends JpaRepository<MarathonCourseEntity, Long> {
    Optional<MarathonCourseEntity> findByCourse(String course);
}
