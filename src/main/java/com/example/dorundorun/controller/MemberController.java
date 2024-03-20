package com.example.dorundorun.controller;

import com.example.dorundorun.dto.*;
import com.example.dorundorun.entity.RunningSpotEntity;
import com.example.dorundorun.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    //생성자 주입
    private final MemberService memberService;
    private final CrewService crewService;
    private final BoardService boardService;
    private final RunningSpotService runningSpotService;
    private final BadgeService badgeService;
    private final RunningService runningService;

    @GetMapping("/join")
    public String saveForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "login";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
//        MemberDTO loginResult = memberService.login(memberDTO);
//        if(loginResult != null){
//            session.setAttribute("loginUser",loginResult.getUsername());
//            return "main";
//        }else{
//            return "index";
//        }
//    }

    @PostMapping("/idCheck")
    public @ResponseBody String idCheck(@RequestParam("username") String username){
        System.out.println(username);
        String checkResult = memberService.idCheck(username);
        return checkResult;
    }

    @PostMapping("/nicknameCheck")
    public @ResponseBody String nicknameCheck(@RequestParam("memberNickname") String memberNickname){
        String checkResult = memberService.nicknameCheck(memberNickname);
        return checkResult;
    }

    @GetMapping("/main")
    public String mainP(Authentication authentication, Model model){
        String username = authentication.getName();
        MemberDTO member = memberService.getMember(username);
        List<CrewDTO> crewDTOList = crewService.findAll();
        List<BoardDTO> boardDTOList = boardService.findBoardTitle();
        List<RunningSpotEntity> saveRunningSpot = runningSpotService.getAllRunningSpots();
        List<RunningDTO> runningDTOList = runningService.findAll();


        Boolean isFirstLogin = badgeService.getBadgeDTOByMemberDTOAndBadgeId(member,1L);

        int dateDifference = badgeService.dateDifference(member);

        model.addAttribute("runningList", runningDTOList);
        model.addAttribute("runningSpot", saveRunningSpot);
        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("crewList", crewDTOList);
        model.addAttribute("dateDiffer", dateDifference);
        model.addAttribute("isFirstLogin", isFirstLogin);
        model.addAttribute("member", member);
        return "main";

    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model){
        String username = authentication.getName();
        MemberDTO memberDTO = memberService.getMember(username);

        List<BadgeDTO> badgeDTOS = badgeService.getBadgeDTOListByMemberDTO(memberDTO);

        model.addAttribute("badgeList", badgeDTOS);
        model.addAttribute("member",memberDTO);
        return "profile";
    }

    @GetMapping("/profileupdate")
    public String updateForm(Authentication authentication, Model model){
        MemberDTO memberDTO = memberService.getMember(authentication.getName());
        List<BadgeDTO> badgeDTOS = badgeService.getBadgeDTOListByMemberDTO(memberDTO);

        model.addAttribute("badgeList", badgeDTOS);
        model.addAttribute("member",memberDTO);
        return "updateForm";
    }

    @PostMapping("/profileupdate")
    public String profileupdate(@ModelAttribute MemberDTO memberDTO, Authentication authentication){
        if(memberDTO.getUsername()!=null && memberDTO.getMemberAddr()!=null && memberDTO.getPassword()!=null
                && memberDTO.getMemberBirth()!=null && memberDTO.getMemberPostCode()!=null && memberDTO.getMemberNickname()!=null
                && memberDTO.getMemberName()!=null && memberDTO.getMemberEmail()!=null && memberDTO.getMemberDetailAddr() !=null
                && memberDTO.getMemberRefAddr() !=null) {
            String username = authentication.getName();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iter = authorities.iterator();
            GrantedAuthority auth = iter.next();
            String role = auth.getAuthority();

            memberDTO.setUsername(username);
            memberDTO.setRole(role);
            memberService.profileupdate(memberDTO);
            return "redirect:/member/profile";
        }else{
            return "updateForm";
        }
    }

    @GetMapping("/select_update")
    public String selectUpdateForm(Authentication authentication, Model model){
        MemberDTO memberDTO = memberService.getMember(authentication.getName());
        model.addAttribute("member",memberDTO);
        return "selectForm";
    }

    @PostMapping("/select_update")
    public String selectUpdate(@ModelAttribute MemberDTO memberDTO, Authentication authentication){
        String username = authentication.getName();
        memberDTO.setUsername(username);
        memberService.selectUpdate(memberDTO);

        return "redirect:/member/profile";
    }

    @GetMapping("/quit")
    public String quitMember(Authentication authentication){
        String username = authentication.getName();

        memberService.quitMember(username);

        return "redirect:/logout";
    }

    @GetMapping("/runningRecordList")
    public String getRunningRecordList(Authentication authentication, Model model){
        String username = authentication.getName();
        MemberDTO member = memberService.getMember(username);

        List<RunningRecordDTO> runningRecordDTOList = memberService.getRunningRecordListByMemberDTO(member);
        int runningBadge = badgeService.getBadgeDTOByMemberDTOAndRecordDTOList(member);

        model.addAttribute("runningBadge", runningBadge);
        model.addAttribute("member", member);
        model.addAttribute("runningRecordList", runningRecordDTOList);

        return "runningRecordList";
    }

    @GetMapping("/getRunningRecord")
    public String getRunningRecord(){
        return "getRunningRecord";
    }

    @PostMapping("/saveRunningRecord")
    public String saveRunningRecord(@ModelAttribute RunningRecordDTO runningRecordDTO, Model model, Authentication authentication){

        String username = authentication.getName();
        MemberDTO memberDTO = memberService.getMember(username);

        memberService.saveRunningRecord(memberDTO, runningRecordDTO);

        return "redirect:/member/runningRecordList";
    }

    @GetMapping("/findId")
    public String findIdForm(){
        return "findIdForm";
    }

    @PostMapping("/findId")
    public String findId(@ModelAttribute MemberDTO memberDTO, Model model){
        MemberDTO member = memberService.findUserNameByPhone(memberDTO);
        model.addAttribute("memberId",member.getUsername());

        return "checkId";
    }

    @GetMapping("/findPW")
    public String findPWForm(@ModelAttribute MemberDTO memberDTO){
        return "findPWForm";
    }

    @PostMapping("/findPW")
    public String findPW(@ModelAttribute MemberDTO memberDTO, Model model){
        memberService.changePasswordByPhone(memberDTO);

        return "login";
    }
}
