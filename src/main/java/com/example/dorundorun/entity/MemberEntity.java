package com.example.dorundorun.entity;

import com.example.dorundorun.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member")
public class MemberEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "memberId", unique = true)
    private String username;

    @Column(name = "memberPassword")
    private String password;

    @Column
    private String memberName;

    @Column(unique = true, nullable = false)
    private String memberNickname;

    @Column
    private String memberTel;

    @Column
    private String memberAddr;

    @Column
    private String memberDetailAddr;

    @Column
    private String memberPostCode;

    @Column
    private String memberRefAddr;

    @Column
    private String memberRefAddr1;

    @Column
    private String memberRefAddr2;

    @Column
    private String memberRefAddr3;

    @Column
    private String memberAddr1;

    @Column
    private String memberAddr2;

    @Column
    private String memberAddr3;

    @Column
    private String memberX1;

    @Column
    private String memberX2;

    @Column
    private String memberX3;

    @Column
    private String memberY1;

    @Column
    private String memberY2;

    @Column
    private String memberY3;

    @Column
    private String memberEmail;

    @Column(columnDefinition = "BIGINT DEFAULT '0'")
    private Long memberCelebrity;

    @Column(columnDefinition = "VARCHAR(100) DEFAULT '0'", name = "memberAuth")
    private String role;

    @Column
    private String memberBirth;

    @Column
    private Long countLogin;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardCommentEntity> boardCommentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardLikeEntity> boardLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewMemberEntity> crewMemberEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardEntity> crewBoardEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardLikeEntity> crewBoardLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardCommentEntity> crewBoardCommentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GetBadgeEntity> getBadgeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RunningRecordEntity> runningRecordEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewRunningMemberEntity> crewRunningMemberEntityList = new ArrayList<>();

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername(memberDTO.getUsername());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberNickname(memberDTO.getMemberNickname());
        memberEntity.setMemberTel(memberDTO.getMemberTel());
        memberEntity.setMemberAddr(memberDTO.getMemberAddr());
        memberEntity.setMemberDetailAddr(memberDTO.getMemberDetailAddr());
        memberEntity.setMemberRefAddr(memberDTO.getMemberRefAddr());
        memberEntity.setMemberRefAddr1(memberDTO.getMemberRefAddr1());
        memberEntity.setMemberRefAddr2(memberDTO.getMemberRefAddr2());
        memberEntity.setMemberRefAddr3(memberDTO.getMemberRefAddr3());
        memberEntity.setMemberPostCode(memberDTO.getMemberPostCode());
        memberEntity.setMemberAddr1(memberDTO.getMemberAddr1());
        memberEntity.setMemberAddr2(memberDTO.getMemberAddr2());
        memberEntity.setMemberAddr3(memberDTO.getMemberAddr3());
        memberEntity.setMemberX1(memberDTO.getMemberX1());
        memberEntity.setMemberX2(memberDTO.getMemberX2());
        memberEntity.setMemberX3(memberDTO.getMemberX3());
        memberEntity.setMemberY1(memberDTO.getMemberY1());
        memberEntity.setMemberY2(memberDTO.getMemberY2());
        memberEntity.setMemberY3(memberDTO.getMemberY3());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberCelebrity(memberDTO.getMemberCelebrity());
        memberEntity.setRole(memberDTO.getRole());
        memberEntity.setMemberBirth(memberDTO.getMemberBirth());

        return memberEntity;
    }


    public static MemberEntity toMemberUpdateMustBe(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setUsername(memberDTO.getUsername());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberNickname(memberDTO.getMemberNickname());
        memberEntity.setMemberTel(memberDTO.getMemberTel());
        memberEntity.setMemberAddr(memberDTO.getMemberAddr());
        memberEntity.setMemberDetailAddr(memberDTO.getMemberDetailAddr());
        memberEntity.setMemberRefAddr(memberDTO.getMemberRefAddr());
        memberEntity.setMemberRefAddr1(memberDTO.getMemberRefAddr1());
        memberEntity.setMemberRefAddr2(memberDTO.getMemberRefAddr2());
        memberEntity.setMemberRefAddr3(memberDTO.getMemberRefAddr3());
        memberEntity.setMemberPostCode(memberDTO.getMemberPostCode());
        memberEntity.setMemberAddr1(memberDTO.getMemberAddr1());
        memberEntity.setMemberAddr2(memberDTO.getMemberAddr2());
        memberEntity.setMemberAddr3(memberDTO.getMemberAddr3());
        memberEntity.setMemberX1(memberDTO.getMemberX1());
        memberEntity.setMemberX2(memberDTO.getMemberX2());
        memberEntity.setMemberX3(memberDTO.getMemberX3());
        memberEntity.setMemberY1(memberDTO.getMemberY1());
        memberEntity.setMemberY2(memberDTO.getMemberY2());
        memberEntity.setMemberY3(memberDTO.getMemberY3());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberCelebrity(memberDTO.getMemberCelebrity());
        memberEntity.setRole(memberDTO.getRole());
        memberEntity.setCountLogin(0L);
        memberEntity.setMemberBirth(memberDTO.getMemberBirth());

        return memberEntity;
    }
}
