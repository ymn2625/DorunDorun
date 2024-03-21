package com.example.dorundorun.service;

import com.example.dorundorun.dto.*;
import com.example.dorundorun.entity.*;
import com.example.dorundorun.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrewRunningService {

    private final CrewRunningRepository crewRunningRepository;
    private final CrewRunningMemberRepository crewRunningMemberRepository;
    private final RunningSpotRepository runningSpotRepository;
    private final MemberRepository memberRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRepository crewRepository;

    public Boolean findByCrewRunningDTO(CrewRunningDTO crewRunningDTO, String username) {
        CrewRunningEntity crewRunningEntity = CrewRunningEntity.toFindCrewRunningEntity(crewRunningDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<CrewRunningMemberEntity> crewRunningMemberEntity = crewRunningMemberRepository.findByCrewRunningEntityAndMemberEntity(crewRunningEntity, memberEntity);
        return crewRunningMemberEntity.isPresent();
    }


    public List<CrewRunningDTO> findAll() {
        List<CrewRunningEntity> crewRunningEntityList = crewRunningRepository.findAll();
        List<CrewRunningDTO> crewRunningDTOList = new ArrayList<>();

        for(CrewRunningEntity crewRunningEntity : crewRunningEntityList){
            CrewRunningDTO crewRunningDTO = CrewRunningDTO.toCrewRunningDTO(crewRunningEntity);
            int countCrewRunningMember = crewRunningMemberRepository.countCrewRunningMember(crewRunningDTO.getCrewRunningId());
            crewRunningDTO.setCrewRunningMemberCount(countCrewRunningMember);
            crewRunningDTOList.add(crewRunningDTO);
        }
        return crewRunningDTOList;
    }

    public List<String> getSpotNamesForCrewRunningList(List<CrewRunningDTO> crewRunningDTOList) {
        List<String> spotNamesList = new ArrayList<>();
        for (CrewRunningDTO crewRunningDTO : crewRunningDTOList) {
            List<String> spotNamesByCrewRunningId = runningSpotRepository.findSpotNamesByCrewRunningId(crewRunningDTO.getCrewRunningId());
            spotNamesList.addAll(spotNamesByCrewRunningId);
        }
        return spotNamesList;
    }

    public List<RunningSpotDTO> getAllRunningSpot() {
        List<RunningSpotEntity> runningSpotEntityList = runningSpotRepository.findAll();
        List<RunningSpotDTO> runningSpotDTOList = new ArrayList<>();
        for (RunningSpotEntity entity : runningSpotEntityList) {
            RunningSpotDTO dto = new RunningSpotDTO();
            dto.setSpotId(entity.getSpotId());
            dto.setSpotName(entity.getSpotName());
            // 필요에 따라 다른 속성 추가~
            runningSpotDTOList.add(dto);
        }
        return runningSpotDTOList;
    }

    public CrewRunningDTO crewRunningCreate(CrewRunningDTO crewRunningDTO, String username, String crewRunningDateString, Long crewId) {

        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); // 폼에서 전송된 날짜 문자열을 파싱하기 위한 포맷 설정
            Date crewRunningDate = dateFormat.parse(crewRunningDateString); // 폼에서 전송된 날짜 문자열을 Date 객체로 변환

            CrewRunningEntity crewRunningEntity = CrewRunningEntity.toSaveCrewRunningEntity(crewRunningDTO);
            crewRunningEntity.setCrewRunningDate(crewRunningDate); // 생성된 날짜를 RunningEntity에 설정
            crewRunningRepository.save(crewRunningEntity);

            CrewEntity crewEntity = crewRepository.findById(crewId).get();
            CrewMemberEntity crewMemberEntity = crewMemberRepository.findByCrewEntityAndMemberEntity(crewEntity ,memberEntity).get();

            // RunningMemberEntity 생성 및 저장
            CrewRunningMemberEntity crewRunningMemberEntity = new CrewRunningMemberEntity();
            crewRunningMemberEntity.setCrewRunningRole("CREW_RUNNING_ADMIN");
            crewRunningMemberEntity.setCrewRunningEntity(crewRunningEntity);
            crewRunningMemberEntity.setMemberEntity(memberEntity);
            crewRunningMemberEntity.setCrewMemberEntity(crewMemberEntity);

            crewRunningMemberRepository.save(crewRunningMemberEntity);

            return CrewRunningDTO.toCrewRunningDTO(crewRunningEntity);

        } catch (ParseException e) {
            // 날짜 파싱 오류가 발생한 경우 예외 처리
            e.printStackTrace();
            // 예외 처리 방법에 따라 수정할 필요가 있습니다.
            return null;
        }
    }

    public int countCrewRunning(CrewRunningDTO crewRunningDTO) {
        return crewRunningMemberRepository.countCrewRunningMember(crewRunningDTO.getCrewRunningId());
    }

    public CrewRunningDTO findById(Long runningId) {
        CrewRunningEntity crewRunningEntity = crewRunningRepository.findById(runningId).get();

        return CrewRunningDTO.toCrewRunningDTO(crewRunningEntity);
    }

    public List<CrewRunningMemberDTO> crewRunningMember(CrewRunningDTO crewRunningDTO) {
        CrewRunningEntity crewRunningEntity = CrewRunningEntity.toFindCrewRunningEntity(crewRunningDTO);
        List<CrewRunningMemberEntity> crewRunningMemberEntityList = crewRunningMemberRepository.findAllByCrewRunningEntityOrderByCrewRunningMemberId(crewRunningEntity);
        List<CrewRunningMemberDTO> crewRunningMemberDTOList = new ArrayList<>();
        for(CrewRunningMemberEntity crewRunningMemberEntity : crewRunningMemberEntityList){
            CrewRunningMemberDTO crewRunningMemberDTO = CrewRunningMemberDTO.toCrewRunningMemberDTO(crewRunningMemberEntity);
            crewRunningMemberDTOList.add(crewRunningMemberDTO);
        }

        return crewRunningMemberDTOList;
    }

    public Boolean findByCrewRunningMemberAuth(CrewRunningDTO crewRunningDTO, String username) {
        CrewRunningEntity crewRunningEntity = CrewRunningEntity.toFindCrewRunningEntity(crewRunningDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<CrewRunningMemberEntity> crewRunningMemberEntity = crewRunningMemberRepository.findByCrewRunningEntityAndMemberEntity(crewRunningEntity, memberEntity);
        return crewRunningMemberEntity.map(entity -> entity.getCrewRunningRole().equals("CREW_RUNNING_ADMIN")).orElse(false);
    }

    public void joinCrewRunning(String username, Long runningId) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        CrewRunningEntity crewRunningEntity = crewRunningRepository.findById(runningId).get();

        CrewRunningMemberEntity crewRunningMemberEntity = new CrewRunningMemberEntity();
        crewRunningMemberEntity.setMemberEntity(memberEntity);
        crewRunningMemberEntity.setCrewRunningEntity(crewRunningEntity);
        crewRunningMemberEntity.setCrewRunningRole("CREW_RUNNING_MEMBER");

        crewRunningMemberRepository.save(crewRunningMemberEntity);
    }

    public void update(CrewRunningDTO crewRunningDTO) {
        // 기존 러닝 정보 가져오기
        CrewRunningEntity existingCrewRunningEntity = crewRunningRepository.findById(crewRunningDTO.getCrewRunningId()).orElse(null);
        if (existingCrewRunningEntity == null) {
            // 해당 ID의 러닝 정보가 없으면 업데이트 불가능
            throw new RuntimeException("Running with id " + crewRunningDTO.getCrewRunningId() + " not found.");
        }

        // 수정된 데이터와 기존 데이터 비교하여 변경된 부분만 업데이트
        if (crewRunningDTO.getCrewRunningName() != null) {
            existingCrewRunningEntity.setCrewRunningName(crewRunningDTO.getCrewRunningName());
        }
        if (crewRunningDTO.getCrewRunningContent() != null) {
            existingCrewRunningEntity.setCrewRunningContent(crewRunningDTO.getCrewRunningContent());
        }
        if (crewRunningDTO.getSpotId() != null) {
            RunningSpotEntity spotEntity = runningSpotRepository.findById(crewRunningDTO.getSpotId()).orElse(null);
            existingCrewRunningEntity.setRunningSpotEntity(spotEntity);
        }
        if (crewRunningDTO.getCrewRunningDate() != null) {
            existingCrewRunningEntity.setCrewRunningDate(crewRunningDTO.getCrewRunningDate());
        }
        if (crewRunningDTO.getCrewRunningLimit() != null) {
            existingCrewRunningEntity.setCrewRunningLimit(crewRunningDTO.getCrewRunningLimit());
        }


        // 업데이트된 러닝 정보 저장
        crewRunningRepository.save(existingCrewRunningEntity);
    }

    public void deleteById(Long runningId) {
        crewRunningRepository.deleteById(runningId);
    }

    public void quitCrewRunning(String username, Long runningId) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        CrewRunningEntity crewRunningEntity = crewRunningRepository.findById(runningId).get();

        Optional<CrewRunningMemberEntity> byRunningEntityAndMemberEntity = crewRunningMemberRepository.findByCrewRunningEntityAndMemberEntity(crewRunningEntity, memberEntity);
        if(byRunningEntityAndMemberEntity.isPresent()){
            Long crewRunningMemberId = byRunningEntityAndMemberEntity.get().getCrewRunningMemberId();
            crewRunningMemberRepository.deleteById(crewRunningMemberId);
        }
    }
}
