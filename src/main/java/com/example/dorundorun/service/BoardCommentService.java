package com.example.dorundorun.service;

import com.example.dorundorun.dto.BoardCommentDTO;
import com.example.dorundorun.entity.BoardCommentEntity;
import com.example.dorundorun.entity.BoardEntity;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.repository.BoardCommentRepository;
import com.example.dorundorun.repository.BoardRepository;
import com.example.dorundorun.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    public Long save(BoardCommentDTO commentDTO, String username) {
        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        Optional<BoardEntity> byId = boardRepository.findById(commentDTO.getBoardId());
        if(byId.isPresent()){
            BoardEntity boardEntity = byId.get();
            BoardCommentEntity commentEntity = BoardCommentEntity.toSaveEntity(commentDTO, boardEntity, memberEntity);
            return boardCommentRepository.save(commentEntity).getBoardCommentId();
        }else{
            return null;
        }

    }

    public List<BoardCommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<BoardCommentEntity> commentEntityList = boardCommentRepository.findAllByBoardEntityOrderByBoardCommentIdDesc(boardEntity);

        List<BoardCommentDTO> commentDTOList = new ArrayList<>();
        for(BoardCommentEntity commentEntity:commentEntityList){
            String userNickname = commentEntity.getMemberEntity().getMemberNickname();
            BoardCommentDTO commentDTO = BoardCommentDTO.toCommentDTO(commentEntity, boardId, userNickname);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
