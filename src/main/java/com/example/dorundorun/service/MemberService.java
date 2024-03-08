package com.example.dorundorun.service;

import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Member;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        memberRepository.save(byUsername);
    }
}
