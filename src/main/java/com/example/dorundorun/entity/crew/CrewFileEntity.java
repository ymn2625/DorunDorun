package com.example.dorundorun.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "crewFile")
public class CrewFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewFileId;

    @Column
    private String crewOriginalFileName;

    @Column
    private String crewStoredFileName;

    //1:n관계 + 상속관계
    @ManyToOne(fetch = FetchType.EAGER)  //eager -> 부모를 조회시에 자식을 다 가져옴, Lazy -> 필요한 상황에 내가 호출
    @JoinColumn(name = "crewId")      // table을 만들때 컬럼이름
    private CrewEntity crewEntity;

    public static CrewFileEntity toCrewFileEntity(CrewEntity crew, String originalFilename, String storedFileName) {
        CrewFileEntity crewFileEntity = new CrewFileEntity();
        crewFileEntity.setCrewOriginalFileName(originalFilename);
        crewFileEntity.setCrewStoredFileName(storedFileName);
        crewFileEntity.setCrewEntity(crew);    // pk값이 아닌 부모 엔티티 객체를 넘겨 줘야 함!!!
        return crewFileEntity;
    }
}
