package com.example.dorundorun.dto;

import com.example.dorundorun.entity.RunningEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RunningDTO {

    private Long runningId;
    private String runningName;
    private Date runningDate;
    private String runningContent;

    private Integer runningMemberCount;

    private Long spotId;
    private Long runningLimit;

    //러닝일정 생성 시간
    private LocalDateTime runningCreatedTime;

    //검색용
    private String searchCondition;
    private String searchKeyword;
    // 종료 체크용 필드
    private boolean ended;


    public static RunningDTO toRunningDTO(RunningEntity myRunningEntity) {
        RunningDTO runningDTO = new RunningDTO();
        runningDTO.setRunningId(myRunningEntity.getRunningId());
        runningDTO.setRunningName(myRunningEntity.getRunningName());
        runningDTO.setRunningContent(myRunningEntity.getRunningContent());
        runningDTO.setRunningDate(myRunningEntity.getRunningDate());
        runningDTO.setSpotId(myRunningEntity.getRunningSpotEntity().getSpotId());
        runningDTO.setRunningLimit(myRunningEntity.getRunningLimit());
        runningDTO.setRunningCreatedTime(myRunningEntity.getCreatedTime());
        return runningDTO;
    }

}
