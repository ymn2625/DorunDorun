package com.example.dorundorun.repository;

import com.example.dorundorun.entity.BadgeEntity;
import com.example.dorundorun.entity.GetBadgeEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {
}
