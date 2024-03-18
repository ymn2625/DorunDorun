package com.example.dorundorun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.dorundorun.entity.MarathonEntity;

@Repository
public interface MarathonRepository extends JpaRepository<MarathonEntity, Long> {


    MarathonEntity findFirstByOrderByMarathonIdAsc();
}
