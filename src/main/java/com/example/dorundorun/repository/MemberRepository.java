package com.example.dorundorun.repository;

import com.example.dorundorun.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberNickname(String memberNickname);
    Optional<MemberEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    void deleteByUsername(String username);

}
