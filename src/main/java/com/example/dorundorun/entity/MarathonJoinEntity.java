package com.example.dorundorun.entity;

import com.example.dorundorun.dto.MarathonDTO;
import com.example.dorundorun.dto.MarathonJoinDTO;
import com.example.dorundorun.repository.MemberRepository;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "marathonJoin")
public class MarathonJoinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marathonJoinId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marathonId")
    private MarathonEntity marathonEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marathonCourseId")
    private MarathonCourseEntity marathonCourseEntity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @Column
    private boolean cancel;

    @Column
    private String reward;


    public static MarathonJoinEntity toMarathonJoinEntity(MarathonJoinDTO marathonJoinDTO, MemberRepository memberRepository) {
        MarathonJoinEntity marathonJoinEntity = new MarathonJoinEntity();

        // MemberEntity 가져와서 설정
        MemberEntity memberEntity = memberRepository.findByUsername(marathonJoinDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        marathonJoinEntity.setMemberEntity(memberEntity);

        // 나머지 필드 설정
        marathonJoinEntity.setMarathonJoinId(marathonJoinDTO.getMarathonJoinId());

        MarathonEntity marathonEntity = new MarathonEntity();
        marathonEntity.setMarathonId(marathonJoinDTO.getMarathonId());
        marathonJoinEntity.setMarathonEntity(marathonEntity);

        MarathonCourseEntity marathonCourseEntity = new MarathonCourseEntity();
        marathonCourseEntity.setMarathonCourseId(marathonJoinDTO.getMarathonCourseId());
        marathonJoinEntity.setMarathonCourseEntity(marathonCourseEntity);

        marathonJoinEntity.setCancel(marathonJoinDTO.isCancel());
        marathonJoinEntity.setReward(marathonJoinDTO.getReward());

        return marathonJoinEntity;
    }



}
