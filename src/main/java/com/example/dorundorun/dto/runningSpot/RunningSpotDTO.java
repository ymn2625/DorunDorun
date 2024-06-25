package com.example.dorundorun.dto;

import com.example.dorundorun.entity.runningSpot.RunningSpotEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RunningSpotDTO {

    private Long spotId;
    private String spotName;
    private String spotAddress;
    private String spotX;
    private String spotY;

    public static RunningSpotDTO toRunningSpotDTO(RunningSpotEntity runningSpotEntity) {
        RunningSpotDTO runningSpotDTO = new RunningSpotDTO();
        runningSpotDTO.setSpotId(runningSpotEntity.getSpotId());
        runningSpotDTO.setSpotName(runningSpotEntity.getSpotName());
        runningSpotDTO.setSpotAddress(runningSpotDTO.getSpotAddress());
        runningSpotDTO.setSpotX(runningSpotEntity.getSpotX());
        runningSpotDTO.setSpotY(runningSpotEntity.getSpotY());
        return runningSpotDTO;
    }

}
