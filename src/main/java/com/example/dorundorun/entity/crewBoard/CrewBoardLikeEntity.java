package com.example.dorundorun.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "crwBoardLike")
public class CrewBoardLikeEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewBoardLikeId;

    @Column
    private Integer crewBoardLike;

    @Column
    private Integer crewBoardHate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="memberId")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="crewBoardId")
    private CrewBoardEntity crewBoardEntity;

    public static CrewBoardLikeEntity toCrewBoardLikeEntity(CrewBoardEntity crewBoardEntity, MemberEntity memberEntity) {
        CrewBoardLikeEntity crewBoardLikeEntity = new CrewBoardLikeEntity();
        crewBoardLikeEntity.setCrewBoardEntity(crewBoardEntity);
        crewBoardLikeEntity.setMemberEntity(memberEntity);

        return crewBoardLikeEntity;
    }
}
