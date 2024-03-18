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
@Table(name = "crew")
public class CrewEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewId;

    @Column(unique = true)
    private String crewName;

    @Column
    private String crewDesc;

    @Column
    private Long crewLimit;

    @OneToMany(mappedBy = "crewEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewMemberEntity> crewMemberEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "crewEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewBoardEntity> crewBoardEntityList = new ArrayList<>();

    public static CrewEntity toSaveCrewEntity(CrewDTO crewDTO) {
        CrewEntity crewEntity = new CrewEntity();
        crewEntity.setCrewName(crewDTO.getCrewName());
        crewEntity.setCrewDesc(crewDTO.getCrewDesc());
        crewEntity.setCrewLimit(crewDTO.getCrewLimit());

        return crewEntity;
    }

    public static CrewEntity toFindCrewEntity(CrewDTO crewDTO) {
        CrewEntity crewEntity = new CrewEntity();
        crewEntity.setCrewName(crewDTO.getCrewName());
        crewEntity.setCrewDesc(crewDTO.getCrewDesc());
        crewEntity.setCrewLimit(crewDTO.getCrewLimit());
        crewEntity.setCrewId(crewDTO.getCrewId());

        return crewEntity;
    }
}
