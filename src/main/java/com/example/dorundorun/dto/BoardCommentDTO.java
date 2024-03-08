package com.example.dorundorun.dto;

import com.example.dorundorun.entity.BoardCommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentDTO {
    private Long boardCommentId;
    private String boardCommentContent;
    private Long boardId;
    private String memberId;
    private String memberNickname;
    private LocalDateTime boardCommentCreatedTime;
    private LocalDateTime boardCommentUpdatedTime;

    public static BoardCommentDTO toCommentDTO(BoardCommentEntity commentEntity, Long boardId, String userNickname) {
        BoardCommentDTO commentDTO = new BoardCommentDTO();
        commentDTO.setBoardCommentId(commentEntity.getBoardCommentId());
        commentDTO.setBoardCommentContent(commentEntity.getBoardCommentContent());
        commentDTO.setMemberNickname(userNickname);
        commentDTO.setBoardCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setBoardId(boardId);

        return commentDTO;
    }
}
