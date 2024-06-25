package com.example.dorundorun.dto;

import com.example.dorundorun.entity.board.BoardLikeEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardLikeDTO {
    private Long boardLikeId;
    private String memberId;
    private Long boardId;

    private int boardLike;
    private int boardHate;

    private int countLike;
    private int countHate;


    public static BoardLikeDTO toBoardLikeDTO(BoardLikeEntity boardLikeEntity) {
        BoardLikeDTO boardLikeDTO = new BoardLikeDTO();
        boardLikeDTO.setBoardLikeId(boardLikeEntity.getBoardLikeId());
        boardLikeDTO.setBoardId(boardLikeEntity.getBoardEntity().getBoardId());
        boardLikeDTO.setMemberId(boardLikeEntity.getMemberEntity().getUsername());
        boardLikeDTO.setBoardLike(boardLikeEntity.getBoardLike());
        boardLikeDTO.setBoardHate(boardLikeEntity.getBoardHate());
        return boardLikeDTO;
    }
}
