package com.example.dorundorun.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "crewBoardFile")
public class CrewBoardFileEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewBoardFileId;

    @Column
    private String crewBoardOriginalFileName;

    @Column
    private String crewBoardStoredFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewBoardId")
    private CrewBoardEntity crewBoardEntity;

    public static CrewBoardFileEntity tocrewBoardFileEntity(CrewBoardEntity crewBoardEntity, String originalFileName, String storedFileName){
        CrewBoardFileEntity crewBoardFileEntity = new CrewBoardFileEntity();
        crewBoardFileEntity.setCrewBoardOriginalFileName(originalFileName);
        crewBoardFileEntity.setCrewBoardStoredFileName(storedFileName);
        crewBoardFileEntity.setCrewBoardEntity(crewBoardEntity);    // pk값이 아닌 부모 엔티티 객체를 넘겨 줘야 함!!!
        return crewBoardFileEntity;
    }
}
