package com.example.dorundorun.service;

import com.example.dorundorun.dto.*;
import com.example.dorundorun.entity.*;
import com.example.dorundorun.repository.MemberRepository;
import com.example.dorundorun.repository.RunningMemberRepository;
import com.example.dorundorun.repository.RunningRepository;
import com.example.dorundorun.repository.RunningSpotRepository;
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
public class RunningService {

    private final RunningRepository runningRepository;
    private final MemberRepository memberRepository;
    private final RunningMemberRepository runningMemberRepository;
    private final RunningSpotRepository runningSpotRepository;

    //일정개설
    public RunningDTO runningCreate(RunningDTO runningDTO, String username, String runningDateString) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); // 폼에서 전송된 날짜 문자열을 파싱하기 위한 포맷 설정
            Date runningDate = dateFormat.parse(runningDateString); // 폼에서 전송된 날짜 문자열을 Date 객체로 변환

            RunningEntity runningEntity = RunningEntity.toSaveRunningEntity(runningDTO);
            runningEntity.setRunningDate(runningDate); // 생성된 날짜를 RunningEntity에 설정
            runningRepository.save(runningEntity);

            RunningEntity myRunningEntity = runningRepository.findByRunningName(runningEntity.getRunningName());

            // RunningMemberEntity 생성 및 저장
            RunningMemberEntity runningMemberEntity = new RunningMemberEntity();
            runningMemberEntity.setRunningRole("RUNNING_ADMIN");
            runningMemberEntity.setRunningEntity(myRunningEntity);
            runningMemberEntity.setMemberEntity(memberEntity);

            runningMemberRepository.save(runningMemberEntity);

            return RunningDTO.toRunningDTO(myRunningEntity);

        } catch (ParseException e) {
            // 날짜 파싱 오류가 발생한 경우 예외 처리
            e.printStackTrace();
            // 예외 처리 방법에 따라 수정할 필요가 있습니다.
            return null;
        }
    }

    // RunningSpot 리스트 가져오기
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

    // 리스트에서 스팟가져오기
    public List<String> getSpotNamesForRunningList(List<RunningDTO> runningDTOList) {
        List<String> spotNamesList = new ArrayList<>();
        for (RunningDTO runningDTO : runningDTOList) {
            List<String> spotNamesByRunningId = runningSpotRepository.findSpotNamesByRunningId(runningDTO.getRunningId());
            spotNamesList.addAll(spotNamesByRunningId);
        }
        return spotNamesList;
    }


    //RunningDTO runningId로 찾기
    public RunningDTO findById(Long runningId) {
        RunningEntity runningEntity = runningRepository.findById(runningId).get();

        return RunningDTO.toRunningDTO(runningEntity);
    }

    //RunningDTO로 Running 인원수 확인
    public int countRunning(RunningDTO runningDTO) {
        return runningMemberRepository.countRunningMember(runningDTO.getRunningId());
    }

    //RunningDTOList 찾기
    public List<RunningDTO> findAll() {
        List<RunningEntity> runningEntityList = runningRepository.findAll();
        List<RunningDTO> runningDTOList = new ArrayList<>();

        for(RunningEntity runningEntity : runningEntityList){
            RunningDTO runningDTO = RunningDTO.toRunningDTO(runningEntity);
            int countRunningMember = runningMemberRepository.countRunningMember(runningDTO.getRunningId());
            runningDTO.setRunningMemberCount(countRunningMember);
            runningDTOList.add(runningDTO);
        }
        return runningDTOList;
    }

    //러닝일정 신청 멤버인지?
    public Boolean findByRunningDTO(RunningDTO runningDTO, String username) {
        RunningEntity runningEntity = RunningEntity.toFindRunningEntity(runningDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<RunningMemberEntity> runningMemberEntity = runningMemberRepository.findByRunningEntityAndMemberEntity(runningEntity, memberEntity);
        return runningMemberEntity.isPresent();
    }

    //러닝일정 신청하기
    public void joinRunning(String username, long runningId) {

        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        RunningEntity runningEntity = runningRepository.findById(runningId).get();

        RunningMemberEntity runningMemberEntity = new RunningMemberEntity();
        runningMemberEntity.setMemberEntity(memberEntity);
        runningMemberEntity.setRunningEntity(runningEntity);
        runningMemberEntity.setRunningRole("RUNNING_MEMBER");

        runningMemberRepository.save(runningMemberEntity);
    }

    //러닝일정 참여인원 리스트 찾기
    public List<RunningMemberDTO> runningMember(RunningDTO runningDTO) {
        RunningEntity runningEntity = RunningEntity.toFindRunningEntity(runningDTO);
        List<RunningMemberEntity> runningMemberEntityList = runningMemberRepository.findAllByRunningEntityOrderByRunningMemberId(runningEntity);
        List<RunningMemberDTO> runningMemberDTOList = new ArrayList<>();
        for(RunningMemberEntity runningMemberEntity : runningMemberEntityList){
            RunningMemberDTO runningMemberDTO = RunningMemberDTO.toRunningMemberDTO(runningMemberEntity);
            runningMemberDTOList.add(runningMemberDTO);
        }

        return runningMemberDTOList;
    }

    //일정개설자인가요?
    public Boolean findByRunningMemberAuth(RunningDTO runningDTO, String username) {
        RunningEntity runningEntity = RunningEntity.toFindRunningEntity(runningDTO);
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();

        Optional<RunningMemberEntity> runningMemberEntity = runningMemberRepository.findByRunningEntityAndMemberEntity(runningEntity, memberEntity);
        return runningMemberEntity.map(entity -> entity.getRunningRole().equals("RUNNING_ADMIN")).orElse(false);
    }


    //러닝일정 삭제
    public void deleteById(Long id) {
        runningRepository.deleteById(id);
    }

    //일정 업데이트
    public void update(RunningDTO modifiedRunningDTO) {
        // 기존 러닝 정보 가져오기
        RunningEntity existingRunningEntity = runningRepository.findById(modifiedRunningDTO.getRunningId()).orElse(null);
        if (existingRunningEntity == null) {
            // 해당 ID의 러닝 정보가 없으면 업데이트 불가능
            throw new RuntimeException("Running with id " + modifiedRunningDTO.getRunningId() + " not found.");
        }

        // 수정된 데이터와 기존 데이터 비교하여 변경된 부분만 업데이트
        if (modifiedRunningDTO.getRunningName() != null) {
            existingRunningEntity.setRunningName(modifiedRunningDTO.getRunningName());
        }
        if (modifiedRunningDTO.getRunningContent() != null) {
            existingRunningEntity.setRunningContent(modifiedRunningDTO.getRunningContent());
        }
        if (modifiedRunningDTO.getSpotId() != null) {
            RunningSpotEntity spotEntity = runningSpotRepository.findById(modifiedRunningDTO.getSpotId()).orElse(null);
            existingRunningEntity.setRunningSpotEntity(spotEntity);
        }
        if (modifiedRunningDTO.getRunningDate() != null) {
            existingRunningEntity.setRunningDate(modifiedRunningDTO.getRunningDate());
        }
        if (modifiedRunningDTO.getRunningLimit() != null) {
            existingRunningEntity.setRunningLimit(modifiedRunningDTO.getRunningLimit());
        }


        // 업데이트된 러닝 정보 저장
        runningRepository.save(existingRunningEntity);
    }


    //러닝일정 취소
    public void quitRunning(String username, Long id) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        RunningEntity runningEntity = runningRepository.findById(id).get();

        Optional<RunningMemberEntity> byRunningEntityAndMemberEntity = runningMemberRepository.findByRunningEntityAndMemberEntity(runningEntity, memberEntity);
        if(byRunningEntityAndMemberEntity.isPresent()){
            Long runningMemberId = byRunningEntityAndMemberEntity.get().getRunningMemberId();
            runningMemberRepository.deleteById(runningMemberId);
        }
    }

}
