package com.example.dorundorun.service;

import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.entity.RunningSpotEntity;
import com.example.dorundorun.repository.member.MemberRepository;
import com.example.dorundorun.repository.runningSpot.RunningSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RunningSpotService {

    private final RunningSpotRepository runningSpotRepository;
    private final MemberRepository memberRepository;

    public List<RunningSpotEntity> getAllRunningSpots() {
        return runningSpotRepository.findAll();
    }

    public MemberEntity findById(Long Id) {
        return memberRepository.findById(Id).orElse(null);
    }

    public Optional<RunningSpotEntity> findBySpotId(Long spotId) {
        return runningSpotRepository.findBySpotId(spotId);
    }

    public Optional<MemberEntity> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

}
