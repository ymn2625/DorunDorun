package com.example.dorundorun.entity;

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
@Table(name = "running")
public class RunningEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long runningId;

    @Column
    private String runningName;

    @Column
    private Date runningDate;

    @Column
    private String runningContent;

    @OneToMany(mappedBy = "runningEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RunningMemberEntity> runningMemberEntityList = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spotId")
    private RunningSpotEntity runningSpotEntity;

    @Column
    private Long runningLimit;

    public static RunningEntity toSaveRunningEntity(RunningDTO runningDTO) {
        RunningEntity runningEntity = new RunningEntity();
        runningEntity.setRunningId(runningDTO.getRunningId());
        runningEntity.setRunningName(runningDTO.getRunningName());
        runningEntity.setRunningDate(runningDTO.getRunningDate());
        runningEntity.setRunningContent(runningDTO.getRunningContent());

        RunningSpotEntity runningSpotEntity = new RunningSpotEntity();
        runningSpotEntity.setSpotId(runningDTO.getSpotId());
        runningEntity.setRunningSpotEntity(runningSpotEntity);

        runningEntity.setRunningLimit(runningDTO.getRunningLimit());

        return runningEntity;
    }

    public static RunningEntity toFindRunningEntity(RunningDTO runningDTO) {
        RunningEntity runningEntity = new RunningEntity();
        runningEntity.setRunningId(runningDTO.getRunningId());
        runningEntity.setRunningName(runningDTO.getRunningName());
        runningEntity.setRunningContent(runningDTO.getRunningContent());
        runningEntity.setRunningDate(runningDTO.getRunningDate());

        RunningSpotEntity runningSpotEntity = new RunningSpotEntity();
        runningSpotEntity.setSpotId(runningDTO.getSpotId());

        runningEntity.setRunningLimit(runningDTO.getRunningLimit());

        return runningEntity;

    }
}