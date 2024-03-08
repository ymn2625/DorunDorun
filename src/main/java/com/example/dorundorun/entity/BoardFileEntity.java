package com.example.dorundorun.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "boardFile")
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardFileId;

    @Column
    private String boardOriginalFileName;

    @Column
    private String boardStoredFileName;

    //1:n관계 + 상속관계
    @ManyToOne(fetch = FetchType.LAZY)  //eager -> 부모를 조회시에 자식을 다 가져옴, Lazy -> 필요한 상황에 내가 호출
    @JoinColumn(name = "boardId")      // table을 만들때 컬럼이름
    private BoardEntity boardEntity;

    public static BoardFileEntity toboardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName){
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setBoardOriginalFileName(originalFileName);
        boardFileEntity.setBoardStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);    // pk값이 아닌 부모 엔티티 객체를 넘겨 줘야 함!!!
        return boardFileEntity;
    }
}
