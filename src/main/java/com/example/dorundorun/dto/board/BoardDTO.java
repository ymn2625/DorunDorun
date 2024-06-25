package com.example.dorundorun.dto;


import com.example.dorundorun.entity.board.BoardEntity;
import com.example.dorundorun.entity.board.BoardFileEntity;
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
public class BoardDTO {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String boardCategory;
    private Long boardHits;
    private String memberId;
    private String memberNickname;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile;    //save.html -> Controller 파일 담는 용도
    private List<String> originalFileName;    //원본파일 이름
    private List<String> storedFileName;      //서버 저장용 파일 이름
    private int fileAttached;           // 파일 첨부 여부(첨부 1, 미첨부 0)

    private String searchCondition;
    private String searchKeyword;


    public BoardDTO(Long boardId, String memberNickname, String boardTitle, String boardCategory, Long boardHits, LocalDateTime createdTime) {
        this.boardId=boardId;
        this.memberNickname=memberNickname;
        this.boardCategory=boardCategory;
        this.boardTitle=boardTitle;
        this.boardHits=boardHits;
        this.boardCreatedTime=createdTime;
    }


    public static BoardDTO toBoardDTO(BoardEntity boardEntity, String memberNickname) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardCategory(boardEntity.getBoardCategory());
        boardDTO.setBoardContent(boardEntity.getBoardContent());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setFileAttached(boardEntity.getFileAttached());
        boardDTO.setBoardId(boardEntity.getBoardId());
        boardDTO.setMemberId(boardEntity.getMemberEntity().getUsername());
        boardDTO.setMemberNickname(memberNickname);
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        if(boardEntity.getFileAttached()==0) {
            boardDTO.setFileAttached(boardEntity.getFileAttached());
        }else{
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            boardDTO.setFileAttached(boardEntity.getFileAttached());
            for(BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getBoardOriginalFileName());
                storedFileNameList.add(boardFileEntity.getBoardStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }

        return boardDTO;
    }
}
