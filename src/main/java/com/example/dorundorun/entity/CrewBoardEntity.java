package com.example.dorundorun.entity;

import com.example.dorundorun.dto.CrewBoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "crewBoard")
public class CrewBoardEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewBoardId;

    @Column
    private String crewBoardTitle;

    @Column
    private String crewBoardContent;

    @Column
    private String crewBoardCategory;

    @Column
    private Long crewBoardHits;

    @Column
    private int crewBoardFileAttached;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @OneToMany(mappedBy = "crewBoardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardLikeEntity> crewBoardLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "crewBoardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardFileEntity> crewBoardFileEntityList=new ArrayList<>();

    @OneToMany(mappedBy = "crewBoardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardCommentEntity> crewBoardCommentEntityList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewId")
    private CrewEntity crewEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewMemberId")
    private CrewMemberEntity crewMemberEntity;

    public static CrewBoardEntity toCrewBoardEntity(CrewBoardDTO crewBoardDTO, MemberEntity memberEntity) {
        CrewBoardEntity crewBoardEntity = new CrewBoardEntity();
        crewBoardEntity.setCrewBoardTitle(crewBoardDTO.getCrewBoardTitle());
        crewBoardEntity.setCrewBoardCategory(crewBoardDTO.getCrewBoardCategory());
        crewBoardEntity.setCrewBoardContent(crewBoardDTO.getCrewBoardContent());
        crewBoardEntity.setCrewBoardHits(0L);
        crewBoardEntity.setCrewBoardFileAttached(0);
        crewBoardEntity.setMemberEntity(memberEntity);

        return crewBoardEntity;
    }


    public static CrewBoardEntity toFileCrewBoardEntity(CrewBoardDTO crewBoardDTO, MemberEntity memberEntity) {
        CrewBoardEntity crewBoardEntity = new CrewBoardEntity();
        crewBoardEntity.setCrewBoardTitle(crewBoardDTO.getCrewBoardTitle());
        crewBoardEntity.setCrewBoardCategory(crewBoardDTO.getCrewBoardCategory());
        crewBoardEntity.setCrewBoardContent(crewBoardDTO.getCrewBoardContent());
        crewBoardEntity.setCrewBoardHits(0L);
        crewBoardEntity.setCrewBoardFileAttached(1);
        crewBoardEntity.setMemberEntity(memberEntity);

        return crewBoardEntity;
    }

    public static CrewBoardEntity toUpdateCrewBoardEntity(CrewBoardDTO crewBoardDTO, MemberEntity memberEntity) {
        CrewBoardEntity crewBoardEntity = new CrewBoardEntity();
        crewBoardEntity.setCrewBoardId(crewBoardDTO.getCrewBoardId());
        crewBoardEntity.setCrewBoardTitle(crewBoardDTO.getCrewBoardTitle());
        crewBoardEntity.setCrewBoardCategory(crewBoardDTO.getCrewBoardCategory());
        crewBoardEntity.setCrewBoardContent(crewBoardDTO.getCrewBoardContent());
        crewBoardEntity.setCrewBoardHits(crewBoardDTO.getCrewBoardHits());
        crewBoardEntity.setCrewBoardFileAttached(0);
        crewBoardEntity.setMemberEntity(memberEntity);

        return crewBoardEntity;
    }
}
