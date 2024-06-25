package com.example.dorundorun.dto;

import com.example.dorundorun.entity.CrewEntity;
import com.example.dorundorun.entity.CrewFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String crewAddress;

    private Integer crewMemberCount;

    //크루 생성, 업데이트 시간
    private LocalDateTime crewCreatedTime;
    private LocalDateTime crewUpdatedTime;

    //검색 관련
    private String searchCondition;
    private String searchKeyword;

    //크루 사진 첨부
    private List<MultipartFile> crewFile;    //save.html -> Controller 파일 담는 용도
    private List<String> originalFileName;    //원본파일 이름
    private List<String> storedFileName;      //서버 저장용 파일 이름
    private int fileAttached;           // 파일 첨부 여부(첨부 1, 미첨부 0)

    public static CrewDTO toCrewDTO(CrewEntity myCrewEntity) {
        CrewDTO crewDTO = new CrewDTO();
        crewDTO.setCrewId(myCrewEntity.getCrewId());
        crewDTO.setCrewName(myCrewEntity.getCrewName());
        crewDTO.setCrewDesc(myCrewEntity.getCrewDesc());
        crewDTO.setCrewLimit(myCrewEntity.getCrewLimit());
        crewDTO.setCrewAddress(myCrewEntity.getCrewAddress());
        crewDTO.setCrewCreatedTime(myCrewEntity.getCreatedTime());
        crewDTO.setCrewUpdatedTime(myCrewEntity.getUpdatedTime());

        if(myCrewEntity.getFileAttached()==0) {
            crewDTO.setFileAttached(myCrewEntity.getFileAttached());
        }else{
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            crewDTO.setFileAttached(myCrewEntity.getFileAttached());
            for(CrewFileEntity crewFileEntity: myCrewEntity.getCrewFileEntityList()){
                originalFileNameList.add(crewFileEntity.getCrewOriginalFileName());
                storedFileNameList.add(crewFileEntity.getCrewStoredFileName());
            }
            crewDTO.setOriginalFileName(originalFileNameList);
            crewDTO.setStoredFileName(storedFileNameList);
        }

        return crewDTO;
    }
}
