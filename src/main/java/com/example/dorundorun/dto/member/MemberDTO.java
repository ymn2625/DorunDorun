package com.example.dorundorun.dto;

import com.example.dorundorun.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long id;
    private String username;
    private String password;
    private String memberName;
    private String memberNickname;
    private String memberTel;
    private String memberAddr;
    private String memberPostCode;
    private String memberRefAddr;
    private String memberRefAddr1;
    private String memberRefAddr2;
    private String memberRefAddr3;
    private String memberDetailAddr;
    private String memberAddr1;
    private String memberAddr2;
    private String memberAddr3;
    private String memberX1;
    private String memberX2;
    private String memberX3;
    private String memberY1;
    private String memberY2;
    private String memberY3;
    private String memberEmail;
    private Long memberCelebrity;
    private String role;
    private String memberBirth;
    private LocalDateTime memberCreatedTime;
    private LocalDateTime memberUpdatedTime;


    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setId(memberEntity.getId());
        memberDTO.setUsername(memberEntity.getUsername());
        memberDTO.setPassword(memberEntity.getPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberNickname(memberEntity.getMemberNickname());
        memberDTO.setMemberTel(memberEntity.getMemberTel());
        memberDTO.setMemberAddr(memberEntity.getMemberAddr());
        memberDTO.setMemberDetailAddr(memberEntity.getMemberDetailAddr());
        memberDTO.setMemberRefAddr(memberEntity.getMemberRefAddr());
        memberDTO.setMemberRefAddr1(memberEntity.getMemberRefAddr1());
        memberDTO.setMemberRefAddr2(memberEntity.getMemberRefAddr2());
        memberDTO.setMemberRefAddr3(memberEntity.getMemberRefAddr3());
        memberDTO.setMemberPostCode(memberEntity.getMemberPostCode());
        memberDTO.setMemberAddr1(memberEntity.getMemberAddr1());
        memberDTO.setMemberAddr2(memberEntity.getMemberAddr2());
        memberDTO.setMemberAddr3(memberEntity.getMemberAddr3());
        memberDTO.setMemberX1(memberEntity.getMemberX1());
        memberDTO.setMemberX2(memberEntity.getMemberX2());
        memberDTO.setMemberX3(memberEntity.getMemberX3());
        memberDTO.setMemberY1(memberEntity.getMemberY1());
        memberDTO.setMemberY2(memberEntity.getMemberY2());
        memberDTO.setMemberY3(memberEntity.getMemberY3());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberCelebrity(memberEntity.getMemberCelebrity());
        memberDTO.setRole(memberEntity.getRole());
        memberDTO.setMemberBirth(memberEntity.getMemberBirth());

        return memberDTO;
    }
}
