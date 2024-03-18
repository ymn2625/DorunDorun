package com.example.dorundorun.controller;

import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.dto.RunningSpotDTO;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.entity.RunningSpotEntity;
import com.example.dorundorun.service.MemberService;
import com.example.dorundorun.service.RunningSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
public class RunningSpotController {

    private final RunningSpotService runningSpotService;
    private final MemberService memberService;

    @GetMapping("/")
    public String spot(Model model, Authentication authentication) {
        List<RunningSpotEntity> spot = runningSpotService.getAllRunningSpots();
        String username = authentication.getName();
        MemberDTO member = memberService.getMember(username);

        model.addAttribute("member", member);
        model.addAttribute("spot", spot);
        return "place";
    }

    @GetMapping("/runningSpot")
    public String saveSpot(Model model, Authentication authentication) {
        List<RunningSpotEntity> saveRunningSpot = runningSpotService.getAllRunningSpots();
        model.addAttribute("saveRunningSpot", saveRunningSpot);

        String member = authentication.getName();
        Optional<MemberEntity> user = runningSpotService.findByUsername(member);
        MemberEntity memberEntity1 = runningSpotService.findById(user.get().getId());

        model.addAttribute("member", memberEntity1);
        return "runningSpot";
    }

    @GetMapping("/detail/{id}")
    public String detailSpot(@PathVariable("id") Long spotId, Model model) {
        Optional<RunningSpotEntity> spotEntity = runningSpotService.findBySpotId(spotId);

        if (spotEntity.isPresent()) {
            RunningSpotEntity spot = spotEntity.get();
            model.addAttribute("spot", spot);
        }

        return "runningSpotDetail";
    }



}
