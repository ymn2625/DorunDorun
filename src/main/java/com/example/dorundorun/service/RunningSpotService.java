package com.example.dorundorun.service;

import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.dto.RunningSpotDTO;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.entity.RunningSpotEntity;
import com.example.dorundorun.repository.MemberRepository;
import com.example.dorundorun.repository.RunningSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningSpotService {

    private final RunningSpotRepository runningSpotRepository;
    private final MemberRepository memberRepository;

    public List<RunningSpotEntity> getAllRunningSpots() {
        return runningSpotRepository.findAll();
    }

    public List<MemberDTO> getRunningSpot() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntityList){
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberAddr1(memberEntity.getMemberAddr1());
            memberDTO.setMemberAddr2(memberEntity.getMemberAddr2());
            memberDTO.setMemberAddr3(memberEntity.getMemberAddr3());

            memberDTO.setMemberX1(memberEntity.getMemberX1());
            memberDTO.setMemberY1(memberEntity.getMemberY1());
            memberDTO.setMemberX2(memberEntity.getMemberX2());
            memberDTO.setMemberY2(memberEntity.getMemberY2());
            memberDTO.setMemberX3(memberEntity.getMemberX3());
            memberDTO.setMemberY3(memberEntity.getMemberY3());

            memberDTOList.add(memberDTO);
        }
        return memberDTOList;
    }
}
