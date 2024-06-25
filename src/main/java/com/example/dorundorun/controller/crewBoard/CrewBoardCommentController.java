package com.example.dorundorun.controller;

import com.example.dorundorun.dto.crewBoard.CrewBoardCommentDTO;
import com.example.dorundorun.service.crewBoard.CrewBoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/crewBoard_comment")
public class CrewBoardCommentController {

    private final CrewBoardCommentService crewBoardCommentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CrewBoardCommentDTO commentDTO, Authentication authentication){
        System.out.println("commentDTO = " + commentDTO);
        String username = authentication.getName();
        Long saveResult = crewBoardCommentService.save(commentDTO, username);
        if(saveResult != null){
            //작성 성공하면 댓글목록을 가져와서 리턴
            //댓글목록: 해당 게시글의 댓글 전체
            List<CrewBoardCommentDTO> commentDTOList = crewBoardCommentService.findAll(commentDTO.getCrewBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
    }
}
