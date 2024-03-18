package com.example.dorundorun.entity;

import com.example.dorundorun.dto.CrewDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "crewMember")
public class CrewMemberEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewMemberId;

    @Column(name = "crewMemberAuth")
    private String role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewId")
    private CrewEntity crewEntity;

    @OneToMany(mappedBy = "crewMemberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardEntity> crewBoardEntityList = new ArrayList<>();
}
