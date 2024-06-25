package com.example.dorundorun.entity;

import com.example.dorundorun.dto.RunningSpotDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "runningSpot")
public class RunningSpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @Column
    private String spotName;

    @Column
    private String spotAddress;

    @Column
    private String spotX;

    @Column
    private String spotY;

    public static RunningSpotEntity toRunningSpotEntity(RunningSpotDTO runningSpotDTO){
        RunningSpotEntity runningSpotEntity = new RunningSpotEntity();
        runningSpotEntity.setSpotId(runningSpotDTO.getSpotId());
        runningSpotEntity.setSpotName(runningSpotDTO.getSpotName());
        runningSpotEntity.setSpotAddress(runningSpotDTO.getSpotAddress());
        runningSpotEntity.setSpotY(runningSpotDTO.getSpotX());
        runningSpotEntity.setSpotY(runningSpotDTO.getSpotY());
        return runningSpotEntity;
    }


}


