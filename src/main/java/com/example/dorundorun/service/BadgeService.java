package com.example.dorundorun.service;

import com.example.dorundorun.dto.BadgeDTO;
import com.example.dorundorun.dto.GetBadgeDTO;
import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.entity.*;
import com.example.dorundorun.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final GetBadgeRepository getBadgeRepository;
    private final BoardRepository boardRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final MemberRepository memberRepository;

    public Boolean getBadgeDTOByMemberDTOAndBadgeId(MemberDTO member, long i) {
        BadgeEntity badgeEntity = badgeRepository.findById(i).get();
        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(member);

        GetBadgeEntity getBadge = new GetBadgeEntity();
        Optional<GetBadgeEntity> byBadgeEntityAndMemberEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(badgeEntity, memberEntity);
        if (byBadgeEntityAndMemberEntity.isPresent()){
            return false;
        }else{
            getBadge.setBadgeEntity(badgeEntity);
            getBadge.setMemberEntity(memberEntity);
            getBadgeRepository.save(getBadge);

            return true;
        }
    }

    public int dateDifference(MemberDTO member) {
        BadgeEntity oneWeekLater = badgeRepository.findById(7L).get();
        BadgeEntity oneMonthLater = badgeRepository.findById(8L).get();
        BadgeEntity sixMonthLater = badgeRepository.findById(9L).get();
        BadgeEntity oneYearLater = badgeRepository.findById(10L).get();

        Optional<MemberEntity> byUsername = memberRepository.findByUsername(member.getUsername());

        GetBadgeEntity getBadge = new GetBadgeEntity();
        if(byUsername.isPresent()){
            MemberEntity memberEntity = byUsername.get();
            LocalDateTime currentDateTime = LocalDateTime.now();

            int dateDiffer = currentDateTime.compareTo(memberEntity.getCreatedTime());

            if(dateDiffer > 365){
                Optional<GetBadgeEntity> byBadgeEntityAndMemberEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(oneYearLater, memberEntity);

                if(byBadgeEntityAndMemberEntity.isPresent()){
                    return 100;
                }else {
                    getBadge.setMemberEntity(memberEntity);
                    getBadge.setBadgeEntity(oneYearLater);
                    getBadgeRepository.save(getBadge);
                    return 4;
                }
            }else if(dateDiffer > 180){
                Optional<GetBadgeEntity> byBadgeEntityAndMemberEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(sixMonthLater, memberEntity);

                if(byBadgeEntityAndMemberEntity.isPresent()){
                    return 100;
                }else {
                    getBadge.setMemberEntity(memberEntity);
                    getBadge.setBadgeEntity(sixMonthLater);
                    getBadgeRepository.save(getBadge);
                    return 3;
                }
            }else if(dateDiffer > 30){
                Optional<GetBadgeEntity> byBadgeEntityAndMemberEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(oneMonthLater, memberEntity);

                if(byBadgeEntityAndMemberEntity.isPresent()){
                    return 100;
                }else {
                    getBadge.setMemberEntity(memberEntity);
                    getBadge.setBadgeEntity(oneMonthLater);
                    getBadgeRepository.save(getBadge);
                    return 2;
                }
            }else if(dateDiffer > 7){
                Optional<GetBadgeEntity> byBadgeEntityAndMemberEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(oneWeekLater, memberEntity);

                if(byBadgeEntityAndMemberEntity.isPresent()){
                    return 100;
                }else {
                    getBadge.setMemberEntity(memberEntity);
                    getBadge.setBadgeEntity(oneWeekLater);
                    getBadgeRepository.save(getBadge);
                    return 1;
                }
            }else{
                return 100;
            }

        }else{
            return 100;
        }


    }

    public Integer getBadgeDTOByMemberDTOForBoard(MemberDTO member) {
        BadgeEntity firstBoardWrite = badgeRepository.findById(2L).get();
        BadgeEntity tenBoardWrite = badgeRepository.findById(3L).get();
        BadgeEntity fiftyBoardWrite = badgeRepository.findById(4L).get();
        BadgeEntity hundredBoardWrite = badgeRepository.findById(5L).get();

        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(member);

        List<BoardEntity> allByMemberEntity = boardRepository.findAllByMemberEntity(memberEntity);

        long countBoardWrite = allByMemberEntity.size();

        GetBadgeEntity getBadge = new GetBadgeEntity();

        if(countBoardWrite >99){
            Optional<GetBadgeEntity> getBadgeEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(hundredBoardWrite, memberEntity);
            if(getBadgeEntity.isPresent()){
                return 100;// 100번째 배지 이미존재 -> 획득X
            }else{
                getBadge.setMemberEntity(memberEntity);
                getBadge.setBadgeEntity(firstBoardWrite);
                getBadgeRepository.save(getBadge);
                return 4; // 100번째 글 작성 배지획득
            }
        }else if(countBoardWrite > 49){
            Optional<GetBadgeEntity> getBadgeEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(fiftyBoardWrite, memberEntity);
            if(getBadgeEntity.isPresent()){
                return 100;// 50번째 배지 이미존재 -> 획득X
            }else{
                getBadge.setMemberEntity(memberEntity);
                getBadge.setBadgeEntity(firstBoardWrite);
                getBadgeRepository.save(getBadge);
                return 3; // 50번째 글 작성 배지획득
            }
        }else if(countBoardWrite > 9){
            Optional<GetBadgeEntity> getBadgeEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(tenBoardWrite, memberEntity);
            if(getBadgeEntity.isPresent()){
                return 100;// 10번째 배지 이미존재 -> 획득X
            }else{
                getBadge.setMemberEntity(memberEntity);
                getBadge.setBadgeEntity(firstBoardWrite);
                getBadgeRepository.save(getBadge);
                return 2; // 10번째 글 작성 배지획득
            }
        }else if(countBoardWrite >0){
            Optional<GetBadgeEntity> getBadgeEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(firstBoardWrite, memberEntity);
            if(getBadgeEntity.isPresent()){
                return 100;// 1번째 배지 이미존재 -> 획득X
            }else{
                getBadge.setMemberEntity(memberEntity);
                getBadge.setBadgeEntity(firstBoardWrite);
                getBadgeRepository.save(getBadge);
                return 1; // 1번째 글 작성 배지획득
            }
        }else {
            return 100; // 100개 이상 작성해서 더이상 배지가 없음 -> 획득 X
        }
    }

    public Boolean getBadgeDTOByMemberDTOForCrew(MemberDTO member) {
        BadgeEntity badgeEntity = badgeRepository.findById(6L).get();
        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(member);

        GetBadgeEntity getBadge = new GetBadgeEntity();

        List<CrewMemberEntity> allByMemberEntity = crewMemberRepository.findAllByMemberEntity(memberEntity);
        if(!allByMemberEntity.isEmpty()){
            Optional<GetBadgeEntity> byBadgeEntityAndMemberEntity = getBadgeRepository.findByBadgeEntityAndMemberEntity(badgeEntity, memberEntity);
            if(byBadgeEntityAndMemberEntity.isPresent()){
                return false;
            }else{
                getBadge.setBadgeEntity(badgeEntity);
                getBadge.setMemberEntity(memberEntity);
                getBadgeRepository.save(getBadge);
                return true;
            }
        }else {
            return false;
        }
    }


    public List<BadgeDTO> getBadgeDTOListByMemberDTO(MemberDTO memberDTO) {

        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(memberDTO);
        List<GetBadgeEntity> getBadgeEntityList = getBadgeRepository.findAllByMemberEntity(memberEntity);
        List<BadgeEntity> badgeEntityList = new ArrayList<>();
        for(GetBadgeEntity getBadge : getBadgeEntityList){
            Optional<BadgeEntity> badgeEntity = badgeRepository.findById(getBadge.getBadgeEntity().getBadgeId());
            badgeEntity.ifPresent(badgeEntityList::add);
        }

        List<BadgeDTO> badgeDTOList = new ArrayList<>();
        for(BadgeEntity badge:badgeEntityList){
            BadgeDTO badgeDTO = BadgeDTO.toBadgeDTO(badge);
            badgeDTOList.add(badgeDTO);
        }

        return badgeDTOList;
    }
}
