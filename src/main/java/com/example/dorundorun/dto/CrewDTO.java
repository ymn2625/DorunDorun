package com.example.dorundorun.dto;

import com.example.dorundorun.entity.CrewEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrewDTO {

    private Long crewId;
    private String crewName;
    private String crewDesc;
    private Long crewLimit;

    private Integer crewMemberCount;

    //크루 생성, 업데이트 시간
    private LocalDateTime crewCreatedTime;
    private LocalDateTime crewUpdatedTime;

    //검색 관련
    private String searchCondition;
    private String searchKeyword;

    public static CrewDTO toCrewDTO(CrewEntity myCrewEntity) {
        CrewDTO crewDTO = new CrewDTO();
        crewDTO.setCrewId(myCrewEntity.getCrewId());
        crewDTO.setCrewName(myCrewEntity.getCrewName());
        crewDTO.setCrewDesc(myCrewEntity.getCrewDesc());
        crewDTO.setCrewLimit(myCrewEntity.getCrewLimit());
        crewDTO.setCrewCreatedTime(myCrewEntity.getCreatedTime());
        crewDTO.setCrewUpdatedTime(myCrewEntity.getUpdatedTime());
        return crewDTO;
    }
}
