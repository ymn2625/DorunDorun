package com.example.dorundorun.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "crewRunningMember")
public class CrewRunningMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewRunningMemberId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crewMemberId")
    private CrewMemberEntity crewMemberEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crewRunningId")
    private CrewRunningEntity crewRunningEntity;

    @Column(name = "RunningMemberAuth")
    private String crewRunningRole;
}
