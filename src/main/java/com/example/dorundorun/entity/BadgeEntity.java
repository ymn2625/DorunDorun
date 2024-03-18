package com.example.dorundorun.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "badge")
public class BadgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;

    @Column
    private String badgeName;

    @Column
    private String badgeDesc;

    @OneToMany(mappedBy = "badgeEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GetBadgeEntity> getBadgeEntityList = new ArrayList<>();

}
