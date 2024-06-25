package com.example.dorundorun.dto.badge;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetBadgeDTO {
    private Long getBadgeId;
    private Long badgeId;
    private String username;
    private String userNickname;
    private Long id;
}
