package com.example.dorundorun.dto;

import com.example.dorundorun.entity.CrewBoardCommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrewBoardCommentDTO {
    private Long crewBoardCommentId;
    private String crewBoardCommentContent;
    private Long crewBoardId;
    private String memberId;
    private String memberNickname;
    private LocalDateTime crewBoardCommentCreatedTime;
    private LocalDateTime crewBoardCommentUpdatedTime;

    public static CrewBoardCommentDTO toCrewBoardCommentDTO(CrewBoardCommentEntity commentEntity, Long boardId, String userNickname) {
        CrewBoardCommentDTO commentDTO = new CrewBoardCommentDTO();
        commentDTO.setCrewBoardCommentId(commentEntity.getCrewBoardCommentId());
        commentDTO.setCrewBoardCommentContent(commentEntity.getCrewBoardCommentContent());
        commentDTO.setMemberNickname(userNickname);
        commentDTO.setCrewBoardCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setCrewBoardId(boardId);

        return commentDTO;
    }
}
