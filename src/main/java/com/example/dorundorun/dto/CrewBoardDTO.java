package com.example.dorundorun.dto;

import com.example.dorundorun.entity.BoardEntity;
import com.example.dorundorun.entity.BoardFileEntity;
import com.example.dorundorun.entity.CrewBoardEntity;
import com.example.dorundorun.entity.CrewBoardFileEntity;
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
public class CrewBoardDTO {
    private Long crewBoardId;
    private String crewBoardTitle;
    private String crewBoardContent;
    private String crewBoardCategory;
    private Long crewBoardHits;
    private String memberId;
    private String memberNickname;
    private Long crewId;
    private Long crewMemberId;
    private LocalDateTime crewBoardCreatedTime;
    private LocalDateTime crewBoardUpdatedTime;

    private List<MultipartFile> crewBoardFile;
    private List<String> crewBoardOriginalFileName;
    private List<String> crewBoardStoredFileName;
    private int crewBoardFileAttached;

    private String crewBoardSearchCondition;
    private String crewBoardSearchKeyword;

    public CrewBoardDTO(Long crewBoardId, String memberNickname, String crewBoardTitle, String crewBoardCategory, Long crewBoardHits, LocalDateTime createdTime) {
        this.crewBoardId=crewBoardId;
        this.memberNickname=memberNickname;
        this.crewBoardTitle=crewBoardTitle;
        this.crewBoardCategory=crewBoardCategory;
        this.crewBoardHits=crewBoardHits;
        this.crewBoardCreatedTime=createdTime;
    }

    public static CrewBoardDTO toCrewBoardDTO(CrewBoardEntity crewBoardEntity, String memberNickname) {
        CrewBoardDTO crewBoardDTO = new CrewBoardDTO();
        crewBoardDTO.setCrewBoardTitle(crewBoardEntity.getCrewBoardTitle());
        crewBoardDTO.setCrewBoardCategory(crewBoardEntity.getCrewBoardCategory());
        crewBoardDTO.setCrewBoardContent(crewBoardEntity.getCrewBoardContent());
        crewBoardDTO.setCrewBoardHits(crewBoardEntity.getCrewBoardHits());
        crewBoardDTO.setCrewBoardFileAttached(crewBoardEntity.getCrewBoardFileAttached());
        crewBoardDTO.setCrewBoardId(crewBoardEntity.getCrewBoardId());
        crewBoardDTO.setMemberId(crewBoardEntity.getMemberEntity().getUsername());
        crewBoardDTO.setMemberNickname(memberNickname);
        crewBoardDTO.setCrewBoardCreatedTime(crewBoardEntity.getCreatedTime());
        crewBoardDTO.setCrewBoardUpdatedTime(crewBoardEntity.getUpdatedTime());
        if(crewBoardEntity.getCrewBoardFileAttached()==0) {
            crewBoardDTO.setCrewBoardFileAttached(crewBoardEntity.getCrewBoardFileAttached());
        }else{
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            crewBoardDTO.setCrewBoardFileAttached(crewBoardEntity.getCrewBoardFileAttached());
            for(CrewBoardFileEntity crewBoardFileEntity: crewBoardEntity.getCrewBoardFileEntityList()){
                originalFileNameList.add(crewBoardFileEntity.getCrewBoardOriginalFileName());
                storedFileNameList.add(crewBoardFileEntity.getCrewBoardStoredFileName());
            }
            crewBoardDTO.setCrewBoardOriginalFileName(originalFileNameList);
            crewBoardDTO.setCrewBoardStoredFileName(storedFileNameList);
        }

        return crewBoardDTO;
    }
}
