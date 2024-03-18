package com.example.dorundorun.entity;

import com.example.dorundorun.dto.CrewRunningDTO;
import com.example.dorundorun.dto.RunningDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "crewRunning")
public class CrewRunningEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewRunningId;
    @Column
    private String crewRunningName;
    @Column
    private Date crewRunningDate;
    @Column
    private String crewRunningContent;

    @OneToMany(mappedBy = "crewRunningEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewRunningMemberEntity> crewRunningMemberEntityList = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spotId")
    private RunningSpotEntity runningSpotEntity;

    @Column
    private Long crewRunningLimit;

}
