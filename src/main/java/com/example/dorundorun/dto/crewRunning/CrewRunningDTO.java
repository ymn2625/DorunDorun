package com.example.dorundorun.dto;

import com.example.dorundorun.entity.CrewRunningEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrewRunningDTO {
    private Long crewRunningId;
    private String crewRunningName;
    private Date crewRunningDate;
    private String crewRunningContent;

    private Integer crewRunningMemberCount;

    private Long spotId;
    private Long crewRunningLimit;

    //러닝일정 생성 시간
    private LocalDateTime crewRunningCreatedTime;

    //검색용
    private String searchCondition;
    private String searchKeyword;

    public static CrewRunningDTO toCrewRunningDTO(CrewRunningEntity crewRunningEntity) {
        CrewRunningDTO crewRunningDTO = new CrewRunningDTO();
        crewRunningDTO.setCrewRunningId(crewRunningEntity.getCrewRunningId());
        crewRunningDTO.setCrewRunningName(crewRunningEntity.getCrewRunningName());
        crewRunningDTO.setCrewRunningContent(crewRunningEntity.getCrewRunningContent());
        crewRunningDTO.setCrewRunningDate(crewRunningEntity.getCrewRunningDate());
        crewRunningDTO.setSpotId(crewRunningEntity.getRunningSpotEntity().getSpotId());
        crewRunningDTO.setCrewRunningLimit(crewRunningEntity.getCrewRunningLimit());
        crewRunningDTO.setCrewRunningCreatedTime(crewRunningEntity.getCreatedTime());
        return crewRunningDTO;
    }
}
