package com.example.dorundorun.dto;

import com.example.dorundorun.entity.RunningRecordEntity;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RunningRecordDTO {
    private Long runningRecordId;
    private Integer runningTimeHH;
    private Integer runningTimeMM;
    private Integer runningTimeSS;
    private Integer runningTimeTenMillis;
    private Integer runningDistanceKm;
    private Integer runningDistanceM;
    private String runningPace;

    private LocalDateTime runningRecordCreatedTime;
    private LocalDateTime runningRecordUpdatedTime;

    private String memberId;
    private String memberNickname;

    public static RunningRecordDTO toRunningRecord(RunningRecordEntity runningRecordEntity) {

        RunningRecordDTO runningRecordDTO = new RunningRecordDTO();
        runningRecordDTO.setRunningRecordId(runningRecordEntity.getRunningRecordId());
        runningRecordDTO.setRunningTimeHH(runningRecordEntity.getRunningTimeHH());
        runningRecordDTO.setRunningTimeMM(runningRecordEntity.getRunningTimeMM());
        runningRecordDTO.setRunningTimeSS(runningRecordEntity.getRunningTimeSS());
        runningRecordDTO.setRunningTimeTenMillis(runningRecordEntity.getRunningTimeTenMillis());
        runningRecordDTO.setRunningDistanceKm(runningRecordEntity.getRunningDistanceKm());
        runningRecordDTO.setRunningDistanceM(runningRecordEntity.getRunningDistanceM());
        runningRecordDTO.setRunningPace(runningRecordEntity.getRunningPace());
        runningRecordDTO.setRunningRecordCreatedTime(runningRecordEntity.getCreatedTime());
        runningRecordDTO.setRunningRecordUpdatedTime(runningRecordEntity.getUpdatedTime());
        runningRecordDTO.setMemberNickname(runningRecordEntity.getMemberEntity().getMemberNickname());
        runningRecordDTO.setMemberId(runningRecordEntity.getMemberEntity().getUsername());

        return runningRecordDTO;
    }
}
