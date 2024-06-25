package com.example.dorundorun.repository;

import com.example.dorundorun.entity.BadgeEntity;
import com.example.dorundorun.entity.GetBadgeEntity;
import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GetBadgeRepository extends JpaRepository<GetBadgeEntity,Long> {
    Optional<GetBadgeEntity> findByBadgeEntityAndMemberEntity(BadgeEntity badgeEntity, MemberEntity memberEntity);

    List<GetBadgeEntity> findAllByMemberEntity(MemberEntity memberEntity);
}
