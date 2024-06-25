package com.example.dorundorun.dto;

import com.example.dorundorun.entity.BadgeEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BadgeDTO {
    private Long badgeId;
    private String badgeName;
    private String badgeDesc;

    public static BadgeDTO toBadgeDTO(BadgeEntity badge) {
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setBadgeId(badge.getBadgeId());
        badgeDTO.setBadgeName(badge.getBadgeName());
        badgeDTO.setBadgeDesc(badge.getBadgeDesc());
        return badgeDTO;
    }
}
