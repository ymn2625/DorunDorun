package com.example.dorundorun.controller;

import com.example.dorundorun.dto.BoardCommentDTO;
import com.example.dorundorun.dto.BoardDTO;
import com.example.dorundorun.dto.BoardLikeDTO;
import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.entity.BoardEntity;
import com.example.dorundorun.service.BoardCommentService;
import com.example.dorundorun.service.BoardService;
import com.example.dorundorun.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardCommentService commentService;

//    @GetMapping("/home")
//    public String boardHome(Model model, Authentication authentication){
//        List<BoardDTO> boardDTOList = boardService.findAll();
//        model.addAttribute("boardList", boardDTOList);
//
//        return "boardHome";
//    }

    @GetMapping("/home")
    public String paging(@PageableDefault(page=1) Pageable pageable, Model model, String searchKeyword, String searchCondition){

        Page<BoardDTO> boardList = boardService.paging(pageable, searchKeyword, searchCondition);


        int blockLimit = 5;//하단에 보여지는 페이지 갯수
        // 사용자가 링크로 넘길 때 가지는 첫번째 페이지
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        // 마지막 페이지는 총 페이지보다 작으면 총 페이지가 마지막 페이지
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage+blockLimit-1 : boardList.getTotalPages();

        Map<String, String> conditionMap = new LinkedHashMap<>();
        conditionMap.put("내용", "boardTitle");
        conditionMap.put("작성자", "memberNickname");

        model.addAttribute("conditionMap", conditionMap);
        model.addAttribute("keyword",searchKeyword);
        model.addAttribute("condition",searchCondition);
        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);

        return "boardHome";
    }



    @GetMapping("/write")
    public String writeForm(Authentication authentication, Model model){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        System.out.println(role);

        model.addAttribute("role",role);

        return "boardWrite";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute BoardDTO boardDTO, Authentication authentication) throws IOException {

        String username = authentication.getName();

        boardService.save(boardDTO, username);

        return "redirect:/board/home";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, Authentication authentication,
                         @PageableDefault(page=1) Pageable pageable){
        boardService.updateHits(id);

        BoardDTO boardDTO = boardService.findById(id);

        String username = authentication.getName();

        MemberDTO memberDTO = memberService.getMember(username);
        String userNickname = memberDTO.getMemberNickname();

        BoardLikeDTO boardLikeDTO = boardService.findByBoardId(id);


        //댓글목록
        List<BoardCommentDTO> commentDTOList = commentService.findAll(id);

        model.addAttribute("board",boardDTO);
        model.addAttribute("username",username);
        model.addAttribute("userNickname",userNickname);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("likeOrHate",boardLikeDTO);

        return "boardDetail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        BoardDTO boardDTO = boardService.findById(id);

        model.addAttribute("board",boardDTO);
        model.addAttribute("role",role);
        return "boardUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model, Authentication authentication){
        String username = authentication.getName();

        BoardDTO board = boardService.update(boardDTO, username);

        model.addAttribute("board",board);
        model.addAttribute("username",username);

        //return을 redirect 방식을 채택하지 않은 것은 redirect 방식 채택시 또 조회수가 오르기 때문이다.
        return "boardDetail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);

        return "redirect:/board/home";
    }

    @PostMapping("/like")
    public ResponseEntity likeProc(@ModelAttribute BoardLikeDTO boardLikeDTO){
        System.out.println("boardLikeDTO = " + boardLikeDTO);
        Boolean likeResult = boardService.like(boardLikeDTO);
        if(likeResult){
            BoardLikeDTO boardLike = boardService.findByBoardId(boardLikeDTO.getBoardId());
            return new ResponseEntity<>(boardLike, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/hate")
    public ResponseEntity hateProc(@ModelAttribute BoardLikeDTO boardLikeDTO){
        Boolean likeResult = boardService.hate(boardLikeDTO);
        if(likeResult){
            BoardLikeDTO boardLike = boardService.findByBoardId(boardLikeDTO.getBoardId());
            return new ResponseEntity<>(boardLike, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
    }

}
