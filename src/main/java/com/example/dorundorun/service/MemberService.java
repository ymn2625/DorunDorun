package com.example.dorundorun.service;

import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.dto.RunningRecordDTO;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.entity.RunningRecordEntity;
import com.example.dorundorun.repository.MemberRepository;
import com.example.dorundorun.repository.RunningRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Member;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RunningRecordRepository runningRecordRepository;

    public void save(MemberDTO memberDTO){
        if(memberRepository.existsByUsername(memberDTO.getUsername())){
            return;
        }else {
            //dto -> entity 변환
            MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
            memberEntity.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
            memberEntity.setRole("ROLE_USER");
            //save 메서드 호출
            memberRepository.save(memberEntity);
        }
    }

//    public MemberDTO login(MemberDTO memberDTO){
//        Optional<MemberEntity> byId = memberRepository.findById(memberDTO.getUsername());
//        if(byId.isPresent()){
//            MemberEntity memberEntity = byId.get();
//            if(memberEntity.getPassword().equals(bCryptPasswordEncoder.encode(memberDTO.getPassword()))){
//                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
//                return dto;
//            }
//            return null;
//        }
//        return null;
//    }

    public String idCheck(String username) {
        Optional<MemberEntity> byId = memberRepository.findByUsername(username);
        if(byId.isPresent()){
            return null;
        }else{
            return "ok";
        }
    }

    public String nicknameCheck(String memberNickname) {
        Optional<MemberEntity> byMemberNickname = memberRepository.findByMemberNickname(memberNickname);
        if(byMemberNickname.isPresent()){
            return null;
        }else {
            return "ok";
        }
    }

    public void profileupdate(MemberDTO memberDTO) {
        MemberEntity byUsername = memberRepository.findByUsername(memberDTO.getUsername()).get();
        MemberDTO member = MemberDTO.toMemberDTO(byUsername);

        member.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
        member.setMemberName(memberDTO.getMemberName());
        member.setMemberNickname(memberDTO.getMemberNickname());
        member.setMemberTel(memberDTO.getMemberTel());
        member.setMemberAddr(memberDTO.getMemberAddr());
        member.setMemberDetailAddr(memberDTO.getMemberDetailAddr());
        member.setMemberRefAddr(memberDTO.getMemberRefAddr());
        member.setMemberPostCode(memberDTO.getMemberPostCode());
        member.setMemberEmail(memberDTO.getMemberEmail());
        member.setMemberCelebrity(memberDTO.getMemberCelebrity());
        member.setMemberBirth(memberDTO.getMemberBirth());

        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(member);
        memberRepository.save(memberEntity);
    }

    public MemberDTO getMember(String username) {
        Optional<MemberEntity> byId = memberRepository.findByUsername(username);
        MemberDTO memberDTO = MemberDTO.toMemberDTO(byId.get());
        return memberDTO;
    }

    public String getNickname(String username){
        Optional<MemberEntity> byId = memberRepository.findByUsername(username);
        String userNickname = byId.get().getMemberNickname();
        return userNickname;
    }

    @Transactional
    public void quitMember(String username) {
        memberRepository.deleteByUsername(username);
    }

    public Long findMemberIdByUsername(String username) {
        // 사용자 이름으로 Member 엔티티를 조회합니다.
        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(username);

        // 조회된 Member 엔티티가 null이 아니라면 해당 사용자의 ID를 반환합니다.
        if (memberEntity.isPresent()) {
            return memberEntity.get().getId();
        } else {
            // 사용자를 찾지 못한 경우 예외 처리를 할 수도 있습니다.
            throw new RuntimeException("User with username " + username + " not found");
        }
    }


    public void selectUpdate(MemberDTO memberDTO) {
        MemberEntity byUsername = memberRepository.findByUsername(memberDTO.getUsername()).get();
        byUsername.setMemberAddr1(memberDTO.getMemberAddr1());
        byUsername.setMemberAddr2(memberDTO.getMemberAddr2());
        byUsername.setMemberAddr3(memberDTO.getMemberAddr3());
        byUsername.setMemberX1(memberDTO.getMemberX1());
        byUsername.setMemberX2(memberDTO.getMemberX2());
        byUsername.setMemberX3(memberDTO.getMemberX3());
        byUsername.setMemberY1(memberDTO.getMemberY1());
        byUsername.setMemberY2(memberDTO.getMemberY2());
        byUsername.setMemberY3(memberDTO.getMemberY3());
        byUsername.setMemberRefAddr1(memberDTO.getMemberRefAddr1());
        byUsername.setMemberRefAddr2(memberDTO.getMemberRefAddr2());
        byUsername.setMemberRefAddr3(memberDTO.getMemberRefAddr3());
        memberRepository.save(byUsername);
    }

    public List<RunningRecordDTO> getRunningRecordListByMemberDTO(MemberDTO member) {
        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(member);
        List<RunningRecordEntity> runningRecordEntityList = runningRecordRepository.findAllByMemberEntity(memberEntity);
        List<RunningRecordDTO> runningRecordDTOList = new ArrayList<>();

        for(RunningRecordEntity runningRecordEntity:runningRecordEntityList){
            RunningRecordDTO runningRecordDTO = RunningRecordDTO.toRunningRecord(runningRecordEntity);
            runningRecordDTOList.add(runningRecordDTO);
        }

        return runningRecordDTOList;
    }

    public void saveRunningRecord(MemberDTO memberDTO, RunningRecordDTO runningRecordDTO) {
        RunningRecordEntity runningRecordEntity = RunningRecordEntity.toSaveRunningRecordEntity(runningRecordDTO);

        //페이스 구하기
        double totalTimeMin = (((double) runningRecordDTO.getRunningTimeHH() * 60) + (double) runningRecordDTO.getRunningTimeMM() + ((double) runningRecordDTO.getRunningTimeSS() / 60) + ((double) runningRecordDTO.getRunningTimeTenMillis() / 60000));
        double totalDistanceKm = ((double) runningRecordDTO.getRunningDistanceKm() + (double) runningRecordDTO.getRunningDistanceM() / 1000);

        double pace = totalTimeMin/totalDistanceKm;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedPace = decimalFormat.format(pace);

        MemberEntity memberEntity = MemberEntity.toMemberUpdateMustBe(memberDTO);
        runningRecordEntity.setMemberEntity(memberEntity);
        runningRecordEntity.setRunningPace(formattedPace);

        runningRecordRepository.save(runningRecordEntity);
    }

    public MemberDTO findUserNameByPhone(MemberDTO memberDTO) {
        Optional<MemberEntity> byMemberTelAndMemberName = memberRepository.findByMemberTelAndMemberName(memberDTO.getMemberTel(), memberDTO.getMemberName());

        if(byMemberTelAndMemberName.isPresent()){
            return MemberDTO.toMemberDTO(byMemberTelAndMemberName.get());
        }else{
            memberDTO.setUsername("가입하신 계정이 없습니다.");
            return memberDTO;
        }
    }

    public void changePasswordByPhone(MemberDTO memberDTO) {
        Optional<MemberEntity> byMemberTelAndMemberName = memberRepository.findByMemberTelAndMemberName(memberDTO.getMemberTel(), memberDTO.getMemberName());

        if(byMemberTelAndMemberName.isPresent()){
            MemberEntity memberEntity = byMemberTelAndMemberName.get();
            memberEntity.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
            memberRepository.save(memberEntity);
        }
    }
}
