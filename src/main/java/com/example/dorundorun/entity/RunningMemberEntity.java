package com.example.dorundorun.entity;

import com.example.dorundorun.dto.RunningMemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "runningMember")
public class RunningMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long runningMemberId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "runningId")
    private RunningEntity runningEntity;

    @Column(name = "RunningMemberAuth")
    private String runningRole;


}
