package com.example.dorundorun.controller;

import com.example.dorundorun.dto.*;
import com.example.dorundorun.dto.crewRunning.CrewRunningDTO;
import com.example.dorundorun.dto.crewRunning.CrewRunningMemberDTO;
import com.example.dorundorun.entity.runningSpot.RunningSpotEntity;
import com.example.dorundorun.repository.runningSpot.RunningSpotRepository;
import com.example.dorundorun.service.crewRunning.CrewRunningService;
import com.example.dorundorun.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.text.SimpleDateFormat;

@Controller
@RequiredArgsConstructor
@RequestMapping("/crewRunning")
public class CrewRunningController {

    private final CrewRunningService crewRunningService;
    private final MemberService memberService;
    private final RunningSpotRepository runningSpotRepository;


    // 리스트 날짜 변환용
    private List<String> formatDateList(List<CrewRunningDTO> crewRunningDTOList) {
        List<String> formattedDates = new ArrayList<>();
        for (CrewRunningDTO crewRunningDTO : crewRunningDTOList) {
            Date crewRunningDate = crewRunningDTO.getCrewRunningDate();
            String formattedDate = formatDate(crewRunningDate);
            formattedDates.add(formattedDate);
        }
        return formattedDates;
    }

    // 리스트 날짜 변환용
    private String formatDate(Date date) {
        // 날짜 형식 변환 로직
        if (date == null) {
            return "언제?";
        }
        Date currentDate = new Date();
        long diff = date.getTime() - currentDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "오 늘 \n" + sdf.format(date);
        } else if (diffDays == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "내 일 \n" + sdf.format(date);
        } else if (diffDays == 2) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "모 레 \n" + sdf.format(date);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd \n HH:mm");
            return sdf.format(date);
        }
    }


    @GetMapping("/home/{crewId}")
    public String paging(@PathVariable Long crewId,
                         Model model, String searchKeyword, String searchCondition, String crewBoardCategory){

        List<CrewRunningDTO> crewRunningDTOList = crewRunningService.findAll();
        List<String> spotNamesList = crewRunningService.getSpotNamesForCrewRunningList(crewRunningDTOList);

        List<String> formattedDates = formatDateList(crewRunningDTOList);

        model.addAttribute("crewId", crewId);
        model.addAttribute("crewRunningList", crewRunningDTOList);
        model.addAttribute("spotNamesList", spotNamesList);
        model.addAttribute("formattedDates", formattedDates);
        return "crewRunning/crewRunningList";
    }

    @GetMapping("/create/{crewId}")
    public String crewRunningCreateForm(@PathVariable Long crewId, Model model){
        // 스팟리스트 부르기
        List<RunningSpotDTO> runningSpotDTOList = crewRunningService.getAllRunningSpot();
        model.addAttribute("spotNames", runningSpotDTOList);
        model.addAttribute("crewId",crewId);
        return "crewRunning/crewRunningCreate";
    }

    @PostMapping("/create/{crewId}")
    public String crewRunningCreate(@ModelAttribute CrewRunningDTO crewRunningDTO,
                                @PathVariable Long crewId,
                                Authentication authentication,
                                @RequestParam("year") String year,
                                @RequestParam("month") String month,
                                @RequestParam("day") String day,
                                @RequestParam("hour") String hour,
                                @RequestParam("minute") String minute){

        try {
            String username = authentication.getName();
            String crewRunningDateString = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00";
            CrewRunningDTO myRunning = crewRunningService.crewRunningCreate(crewRunningDTO, username, crewRunningDateString, crewId);
            Long runningId = myRunning.getCrewRunningId();

            return "redirect:/crewRunning/detail/" + crewId + "/" + runningId;
        }catch (Exception e) {
            e.printStackTrace();
            return "error/error";
        }

    }

    //러닝일정 상세보기
    @GetMapping("/detail/{crewId}/{runningId}")
    public String runningDetail(@PathVariable Long crewId,
                                @PathVariable Long runningId,
                                Model model, Authentication authentication){
        CrewRunningDTO crewRunningDTO = crewRunningService.findById(runningId);
        int countCrewRunningMember = crewRunningService.countCrewRunning(crewRunningDTO);
        String username = authentication.getName();
        String nickname = memberService.getNickname(username);
        Boolean isMember = crewRunningService.findByCrewRunningDTO(crewRunningDTO, username);
        List<CrewRunningMemberDTO> crewRunningMemberDTOList = crewRunningService.crewRunningMember(crewRunningDTO);
        Boolean isAdmin = crewRunningService.findByCrewRunningMemberAuth(crewRunningDTO, username);
        System.out.println(isAdmin);
        Optional<RunningSpotEntity> runningSpot = runningSpotRepository.findById(crewRunningDTO.getSpotId());
        String spotName = runningSpot.map(RunningSpotEntity::getSpotName).orElse("Unknown");


        model.addAttribute("crewRunningDTO", crewRunningDTO);
        model.addAttribute("runningMember", countCrewRunningMember);
        model.addAttribute("isMember", isMember);
        model.addAttribute("crewRunningMemberDTOList", crewRunningMemberDTOList);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("memberNickname", nickname);
        model.addAttribute("spotName", spotName);
        model.addAttribute("crewId",crewId);

        return "crewRunning/crewRunningDetail";
    }

    @GetMapping("/join/{crewId}/{runningId}")
    public String runningJoin(@PathVariable Long crewId,
                              @PathVariable Long runningId,
                              Authentication authentication){
        String username = authentication.getName();

        crewRunningService.joinCrewRunning(username, runningId);

        return "redirect:/crewRunning/detail/"+crewId+"/"+runningId;
    }

    @GetMapping("/update/{crewId}/{runningId}")
    public String crewRunningUpdate(@PathVariable Long crewId,
                                    @PathVariable Long runningId,
                                    Authentication authentication, Model model){
        CrewRunningDTO crewRunningDTO = crewRunningService.findById(runningId);
        String username = authentication.getName();
        int countCrewRunningMember = crewRunningService.countCrewRunning(crewRunningDTO);
        model.addAttribute("crewRunningMember", countCrewRunningMember);
        Boolean isAdmin = crewRunningService.findByCrewRunningMemberAuth(crewRunningDTO, username);
        List<RunningSpotDTO> runningSpotDTOList = crewRunningService.getAllRunningSpot();
        model.addAttribute("spotNames", runningSpotDTOList);
        model.addAttribute("crewRunningDate", crewRunningDTO.getCrewRunningDate());
        model.addAttribute("crewId",crewId);
        if(isAdmin){
            model.addAttribute("crewRunningDTO", crewRunningDTO);
            return "crewRunning/crewRunningUpdate";
        }else {
            return "redirect:/crewRunning/detail/"+crewId+"/"+runningId;
        }
    }

    @PostMapping("/update/{crewId}/{runningId}")
    public String runningUpdate(@PathVariable Long crewId,
                                @PathVariable Long runningId,
                                @ModelAttribute CrewRunningDTO crewRunningDTO, Authentication authentication){
        crewRunningDTO.setCrewRunningId(runningId);
        String username = authentication.getName();
        Boolean isAdmin = crewRunningService.findByCrewRunningMemberAuth(crewRunningDTO, username);
        if(isAdmin){
            crewRunningService.update(crewRunningDTO);
        }
        return "redirect:/crewRunning/detail/"+crewId+"/"+runningId;
    }

    //러닝일정 삭제
    @GetMapping("/delete/{crewId}/{runningId}")
    public String runningDelete(@PathVariable Long crewId,
                                @PathVariable Long runningId,
                                Authentication authentication){

        CrewRunningDTO crewRunningDTO = crewRunningService.findById(runningId);
        String username = authentication.getName();
        Boolean isAdmin = crewRunningService.findByCrewRunningMemberAuth(crewRunningDTO, username);
        if(isAdmin){
            crewRunningService.deleteById(runningId);
        }

        return "redirect:/crewRunning/home/"+crewId;
    }

    //러닝일정 신청취소
    @GetMapping("/quitRunning/{crewId}/{runningId}")
    public String quitRunning(@PathVariable Long crewId,
                              @PathVariable Long runningId,
                              Authentication authentication){
        String username = authentication.getName();

        crewRunningService.quitCrewRunning(username, runningId);

        return "redirect:/crewRunning/detail/"+crewId+"/"+runningId;
    }

}
