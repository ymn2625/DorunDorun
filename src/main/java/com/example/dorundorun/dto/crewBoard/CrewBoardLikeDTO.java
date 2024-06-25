package com.example.dorundorun.dto;

import com.example.dorundorun.entity.CrewBoardLikeEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrewBoardLikeDTO {
    private Long crewBoardLikeId;
    private String memberId;
    private Long crewBoardId;

    private int crewBoardLike;
    private int crewBoardHate;

    private int countCrewBoardLike;
    private int countCrewBoardHate;

    public static CrewBoardLikeDTO toCrewBoardLikeDTO(CrewBoardLikeEntity crewBoardLikeEntity) {
        CrewBoardLikeDTO crewBoardLikeDTO = new CrewBoardLikeDTO();
        crewBoardLikeDTO.setCrewBoardLikeId(crewBoardLikeEntity.getCrewBoardLikeId());
        crewBoardLikeDTO.setCrewBoardId(crewBoardLikeEntity.getCrewBoardEntity().getCrewBoardId());
        crewBoardLikeDTO.setMemberId(crewBoardLikeEntity.getMemberEntity().getUsername());
        crewBoardLikeDTO.setCrewBoardLike(crewBoardLikeEntity.getCrewBoardLike());
        crewBoardLikeDTO.setCrewBoardHate(crewBoardLikeEntity.getCrewBoardHate());
        return crewBoardLikeDTO;
    }
}
