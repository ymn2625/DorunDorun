package com.example.dorundorun.controller;

import com.example.dorundorun.dto.*;
import com.example.dorundorun.entity.CrewMemberEntity;
import com.example.dorundorun.service.CrewBoardCommentService;
import com.example.dorundorun.service.CrewBoardService;
import com.example.dorundorun.service.CrewService;
import com.example.dorundorun.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequiredArgsConstructor
@RequestMapping("/crewBoard")
public class CrewBoardController {

    private final CrewService crewService;
    private final MemberService memberService;
    private final CrewBoardService crewBoardService;
    private final CrewBoardCommentService crewBoardCommentService;

    @GetMapping("/home/{crewId}")
    public String paging(@PageableDefault(page=1) Pageable pageable,
                         @PathVariable Long crewId,
                         Model model,String searchKeyword, String searchCondition, String crewBoardCategory){

        Page<CrewBoardDTO> crewBoardList = crewBoardService.paging(pageable, searchKeyword, searchCondition, crewBoardCategory);


        int blockLimit = 5;//하단에 보여지는 페이지 갯수
        // 사용자가 링크로 넘길 때 가지는 첫번째 페이지
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        // 마지막 페이지는 총 페이지보다 작으면 총 페이지가 마지막 페이지
        int endPage = ((startPage + blockLimit - 1) < crewBoardList.getTotalPages()) ? startPage+blockLimit-1 : crewBoardList.getTotalPages();

        Map<String, String> conditionMap = new LinkedHashMap<>();
        conditionMap.put("내용", "crewBoardTitle");
        conditionMap.put("작성자", "memberNickname");

        model.addAttribute("conditionMap", conditionMap);
        model.addAttribute("keyword",searchKeyword);
        model.addAttribute("condition",searchCondition);
        model.addAttribute("crewBoardList", crewBoardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("category",crewBoardCategory);
        model.addAttribute("crewId",crewId);

        return "crewBoardHome";
    }

    @GetMapping("/write/{crewId}")
    public String writeForm(@PathVariable Long crewId, Authentication authentication, Model model){

        String username = authentication.getName();
        MemberDTO memberDTO = memberService.getMember(username);
        CrewMemberDTO crewMemberDTO = crewService.findCrewMemberDTOByUserNameAndCrewId(username, crewId);

        if(crewMemberDTO.getCrewMemberId()!=null){

            Boolean isManager = crewMemberDTO.getCrewMemberAuth().equals("CREW_ADMIN") || crewMemberDTO.getCrewMemberAuth().equals("CREW_MANAGER");

            model.addAttribute("crewId", crewId);
            model.addAttribute("memberDTO", memberDTO);
            model.addAttribute("isManager", isManager);
        return "crewBoardWriteForm";
        }
    return "redirect:/member/main";
    }

    @PostMapping("/write/{crewId}")
    public String write(@ModelAttribute CrewBoardDTO crewBoardDTO,
                        @PathVariable Long crewId, Authentication authentication) throws IOException {

        String username = authentication.getName();

        crewBoardService.save(crewBoardDTO, username);

        return "redirect:/crewBoard/home/"+crewId;
    }

    @GetMapping("/detail/{crewId}/{crewBoardId}")
    public String detail(@PathVariable Long crewId,
                         @PathVariable Long crewBoardId,
                         Model model,
                         Authentication authentication,
                         @PageableDefault(page=1) Pageable pageable){
        crewBoardService.updateHits(crewBoardId);

        CrewBoardDTO crewBoardDTO = crewBoardService.findById(crewBoardId);

        String username = authentication.getName();

        MemberDTO memberDTO = memberService.getMember(username);
        String userNickname = memberDTO.getMemberNickname();

        CrewBoardLikeDTO crewBoardLikeDTO = crewBoardService.findByCrewBoardId(memberDTO, crewBoardId);


        //댓글목록
        List<CrewBoardCommentDTO> commentDTOList = crewBoardCommentService.findAll(crewBoardId);

        model.addAttribute("crewBoard",crewBoardDTO);
        model.addAttribute("username",username);
        model.addAttribute("userNickname",userNickname);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("likeOrHate",crewBoardLikeDTO);
        model.addAttribute("crewId", crewId);

        return "crewBoardDetail";
    }

    @GetMapping("/update/{crewId}/{crewBoardId}")
    public String updateForm(@PathVariable Long crewId,
                             @PathVariable Long crewBoardId,
                             Model model, Authentication authentication) {
        CrewDTO byId = crewService.findById(crewId);
        String name = authentication.getName();
        Boolean isManager = crewService.findCrewManager(byId, name);

        CrewBoardDTO crewBoardDTO = crewBoardService.findById(crewBoardId);

        model.addAttribute("crewBoard",crewBoardDTO);
        model.addAttribute("isManager",isManager);
        model.addAttribute("crewId", crewId);

        return "crewBoardUpdate";
    }

    @PostMapping("/update/{crewId}/{crewBoardId}")
    public String update(@ModelAttribute CrewBoardDTO crewBoardDTO,
                         @PathVariable Long crewId,
                         @PathVariable Long crewBoardId,
                         Model model, Authentication authentication,
                         @PageableDefault(page=1) Pageable pageable){
        String username = authentication.getName();

        MemberDTO memberDTO = memberService.getMember(username);
        String userNickname = memberDTO.getMemberNickname();

        CrewBoardLikeDTO crewBoardLikeDTO = crewBoardService.findByCrewBoardId(memberDTO, crewBoardId);

        CrewBoardDTO crewBoard = crewBoardService.update(crewBoardDTO, username);
        List<CrewBoardCommentDTO> commentDTOList = crewBoardCommentService.findAll(crewBoardId);


        model.addAttribute("crewId",crewId);
        model.addAttribute("crewBoard",crewBoard);
        model.addAttribute("username",username);
        model.addAttribute("userNickname",userNickname);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("likeOrHate",crewBoardLikeDTO);
        model.addAttribute("crewId", crewId);

        //return을 redirect 방식을 채택하지 않은 것은 redirect 방식 채택시 또 조회수가 오르기 때문이다.
        return "crewBoardDetail";
    }

    @GetMapping("/delete/{crewId}/{crewBoardId}")
    public String delete(@PathVariable Long crewId,
                         @PathVariable Long crewBoardId,
                         Model model){
        crewBoardService.delete(crewBoardId);

        model.addAttribute("crewId",crewId);

        return "redirect:/crewBoard/home/"+crewId;
    }

    @PostMapping("/like")
    public ResponseEntity likeProc(@ModelAttribute CrewBoardLikeDTO crewBoardLikeDTO, Authentication authentication){
        System.out.println("crewBoardLikeDTO = " + crewBoardLikeDTO);
        Boolean likeResult = crewBoardService.like(crewBoardLikeDTO);
        if(likeResult){
            String username = authentication.getName();
            MemberDTO member = memberService.getMember(username);
            CrewBoardLikeDTO crewBoardLike = crewBoardService.findByCrewBoardId(member, crewBoardLikeDTO.getCrewBoardId());
            return new ResponseEntity<>(crewBoardLike, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/hate")
    public ResponseEntity hateProc(@ModelAttribute CrewBoardLikeDTO crewBoardLikeDTO, Authentication authentication){
        Boolean likeResult = crewBoardService.hate(crewBoardLikeDTO);
        if(likeResult){
            String username = authentication.getName();
            MemberDTO member = memberService.getMember(username);
            CrewBoardLikeDTO crewBoardLike = crewBoardService.findByCrewBoardId(member, crewBoardLikeDTO.getCrewBoardId());
            return new ResponseEntity<>(crewBoardLike, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
    }

}
