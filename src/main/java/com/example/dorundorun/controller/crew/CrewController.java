package com.example.dorundorun.controller;

import com.example.dorundorun.dto.crew.CrewDTO;
import com.example.dorundorun.dto.crew.CrewMemberDTO;
import com.example.dorundorun.dto.member.MemberDTO;
import com.example.dorundorun.service.badge.BadgeService;
import com.example.dorundorun.service.crew.CrewService;
import com.example.dorundorun.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/crew")
public class CrewController {

    private final MemberService memberService;
    private final CrewService crewService;
    private final BadgeService badgeService;

    //크루 홈 이동
    @GetMapping("/main")
    public String crewHome(Model model){
        List<CrewDTO> crewDTOList = crewService.findAll();

        model.addAttribute("crewList",crewDTOList);
        return "crew/crewHome";
    }

    //크루 제작폼 이동
    @GetMapping("/create")
    public String crewCreateForm(){
        return "crew/crewCreate";
    }

    //크루 생성
    @PostMapping("/create")
    public String crewCreate(@ModelAttribute CrewDTO crewDTO, Authentication authentication) throws IOException {
        String username = authentication.getName();
        CrewDTO myCrew = crewService.crewCreate(crewDTO, username);
        Long crewId = myCrew.getCrewId();

        return "redirect:/crew/detail/"+crewId;
    }

    //크루 리스트
    @GetMapping("/list")
    public String crewList(Model model){
        List<CrewDTO> crewDTOList = crewService.findAll();

        model.addAttribute("crewList",crewDTOList);

        return "crew/crewList";
    }

    //크루 디테일
    @GetMapping("/detail/{id}")
    public String crewDetail(@PathVariable Long id, Model model, Authentication authentication){
        CrewDTO crewDTO = crewService.findById(id);
        int countCrewMember = crewService.countCrew(crewDTO);
        String username = authentication.getName();
        Boolean isMember = crewService.findByCrewDTO(crewDTO, username);
        List<CrewMemberDTO> crewMemberDTOList = crewService.crewMember(crewDTO);
        Boolean isAdmin = crewService.findByCrewMemberAuth(crewDTO, username);
        Boolean isManager = crewService.findCrewManager(crewDTO, username);

        MemberDTO member = memberService.getMember(username);
        Boolean isFirstCrew = badgeService.getBadgeDTOByMemberDTOForCrew(member);

        model.addAttribute("isFirstCrew", isFirstCrew);
        model.addAttribute("crewDTO", crewDTO);
        model.addAttribute("crewMember", countCrewMember);
        model.addAttribute("isMember", isMember);
        model.addAttribute("crewMemberList", crewMemberDTOList);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isManager", isManager);

        return "crew/crewDetail";
    }

    //크루 가입
    @GetMapping("/join/{id}")
    public String crewJoin(@PathVariable Long id, Authentication authentication){
        String username = authentication.getName();

        crewService.joinCrew(username, id);

        return "redirect:/crew/detail/"+id;
    }

    //크루 업데이트 폼 이동
    @GetMapping("/update/{id}")
    public String crewUpdateForm(@PathVariable Long id, Authentication authentication, Model model){
        CrewDTO crewDTO = crewService.findById(id);
        String username = authentication.getName();
        Boolean isNotManager = crewService.findCrewMember(crewDTO, username);
        if(!isNotManager){
            model.addAttribute("crewDTO", crewDTO);
            return "crew/crewUpdateForm";
        }else {
            return "redirect:/crew/detail/"+id;
        }
    }

    //크루 업데이트
    @PostMapping("/update/{id}")
    public String crewUpdate(@PathVariable Long id,
                             @ModelAttribute CrewDTO crewDTO, Authentication authentication){
        crewDTO.setCrewId(id);
        String username = authentication.getName();
        Boolean isNotManager = crewService.findCrewMember(crewDTO, username);
        if(!isNotManager){
            crewService.update(crewDTO);
        }
        return "redirect:/crew/detail/"+id;
    }

    //크루 삭제
    @GetMapping("/delete/{id}")
    public String crewDelete(@PathVariable Long id, Authentication authentication){

        CrewDTO crewDTO = crewService.findById(id);
        String username = authentication.getName();
        Boolean isAdmin = crewService.findByCrewMemberAuth(crewDTO, username);
        if(isAdmin){
            crewService.deleteById(id);
        }

        return "crew/crewHome";
    }

    //크루 멤버 관리
    @GetMapping("/memberManage/{id}")
    public String crewMemberManageForm(@PathVariable Long id, Authentication authentication, Model model){
        CrewDTO crewDTO = crewService.findById(id);
        String username = authentication.getName();
        Boolean isNotManager = crewService.findCrewMember(crewDTO, username);
        Boolean isAdmin = crewService.findByCrewMemberAuth(crewDTO, username);

        if(!isNotManager){
            List<CrewMemberDTO> crewMemberDTOList = crewService.crewMember(crewDTO);
            model.addAttribute("crewMemberList", crewMemberDTOList);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("crewId", id);
            return "crew/crewMemberManage";
        }else {
            return "redirect:/crew/detail/"+id;
        }
    }

    //매니저 임명
    @GetMapping("/makeManager/{id}")
    public String makeManager(@PathVariable Long id, Authentication authentication, Model model){
        CrewMemberDTO crewMemberDTO = crewService.makeManager(id);
        long crewId= crewMemberDTO.getCrewId();
        CrewDTO crewDTO = crewService.findById(crewId);
        String username = authentication.getName();
        Boolean isNotManager = crewService.findCrewMember(crewDTO, username);
        Boolean isAdmin = crewService.findByCrewMemberAuth(crewDTO, username);

        if(!isNotManager){
            List<CrewMemberDTO> crewMemberDTOList = crewService.crewMember(crewDTO);
            model.addAttribute("crewMemberList", crewMemberDTOList);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("crewId", crewId);
            return "crew/crewMemberManage";
        }else {
            return "redirect:/crew/detail/"+crewId;
        }

    }

    //매니저해제
    @GetMapping("/makeMember/{id}")
    public String makeMember(@PathVariable Long id, Authentication authentication, Model model){
        CrewMemberDTO crewMemberDTO = crewService.makeMember(id);
        long crewId= crewMemberDTO.getCrewId();
        CrewDTO crewDTO = crewService.findById(crewId);
        String username = authentication.getName();
        Boolean isNotManager = crewService.findCrewMember(crewDTO, username);
        Boolean isAdmin = crewService.findByCrewMemberAuth(crewDTO, username);

        if(!isNotManager){
            List<CrewMemberDTO> crewMemberDTOList = crewService.crewMember(crewDTO);
            model.addAttribute("crewMemberList", crewMemberDTOList);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("crewId", crewId);
            return "crew/crewMemberManage";
        }else {
            return "redirect:/crew/detail/"+crewId;
        }

    }

    //관리자 임명
    @GetMapping("/makeAdmin/{id}")
    public String makeAdmin(@PathVariable Long id, Authentication authentication, Model model){
        CrewMemberDTO crewMember = crewService.findCrewMemberDTO(id);
        long crewId= crewMember.getCrewId();
        CrewDTO crew = crewService.findById(crewId);

        CrewMemberDTO crewMemberDTO = crewService.makeAdmin(id, crew);
        CrewDTO crewDTO = crewService.findById(crewMemberDTO.getCrewId());
        String username = authentication.getName();
        Boolean isNotManager = crewService.findCrewMember(crewDTO, username);
        Boolean isAdmin = crewService.findByCrewMemberAuth(crewDTO, username);

        if(!isNotManager){
            List<CrewMemberDTO> crewMemberDTOList = crewService.crewMember(crewDTO);
            model.addAttribute("crewMemberList", crewMemberDTOList);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("crewId", crewId);
            return "crew/crewMemberManage";
        }else {
            return "redirect:/crew/detail/"+crewId;
        }
    }

    //크루 멤버 추방
    @GetMapping("/quitMember/{id}")
    public String quitMember(@PathVariable Long id, Authentication authentication, Model model){
        CrewMemberDTO crewMemberDTO = crewService.deleteMember(id);
        long crewId= crewMemberDTO.getCrewId();
        CrewDTO crewDTO = crewService.findById(crewId);
        String username = authentication.getName();
        Boolean isNotManager = crewService.findCrewMember(crewDTO, username);
        Boolean isAdmin = crewService.findByCrewMemberAuth(crewDTO, username);

        if(!isNotManager){
            List<CrewMemberDTO> crewMemberDTOList = crewService.crewMember(crewDTO);
            model.addAttribute("crewMemberList", crewMemberDTOList);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("crewId", crewId);
            return "crew/crewMemberManage";
        }else {
            return "redirect:/crew/detail/"+crewId;
        }
    }

    //크루 탈퇴
    @GetMapping("/quitCrew/{id}")
    public String quitCrew(@PathVariable Long id, Authentication authentication){
        String username = authentication.getName();

        crewService.quitCrew(username, id);

        return "redirect:/crew/detail/"+id;
    }
}